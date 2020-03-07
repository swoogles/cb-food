package crestedbutte

import crestedbutte.MyApp.loopLogic
import org.scalajs.dom.Node
import org.scalajs.dom.raw.MouseEvent
import zio.{DefaultRuntime, ZIO}
import zio.clock.Clock
import zio.console.Console
import zio.scheduler.SchedulerLive

object UnsafeCallbacks {

  def attachUrlRewriteBehavior(
    pageMode: AppMode.Value,
    environmentDependencies: SchedulerLive
      with Clock
      with Console.Live
      with BrowserLive,
    components: Seq[ComponentData],
  ) =
    ZIO
      .environment[Browser]
      .map { browser =>
        browser.browser
          .querySelector(
            ModalBehavior.id(ElementNames.UrlManipulation.rewriteUrl),
          ) // TODO Find better spot for .id function
          .foreach(
            _.addEventListener(
              "click",
              (_: MouseEvent) => {
                browser.browser
                  .rewriteCurrentUrl("route", "Three_Seasons_Loop")
                new DefaultRuntime {}.unsafeRun(
                  loopLogic(pageMode, components)
                    .provide(environmentDependencies),
                )
              },
            ),
          )
      }

  val attachMenuBehavior =
    ZIO
      .environment[Browser]
      .map(
        browser =>
          browser.browser
            .window()
            .document
            .addEventListener(
              "DOMContentLoaded",
              (_: Any) => {

                def menuCallbackBehavior(node: Node,
                                         browser: Browser) =
                  (_: MouseEvent) =>
                    // Get the target from the "data-target" attribute
                    // POTENTIALLY VERY EXPENSIVE. It's jumping back to the root of the document with this search.
                    browser.browser
                      .querySelector(
                        "#" + node.attributes
                          .getNamedItem("data-target")
                          .value,
                      )
                      .map(_.classList.toggle("is-active"))

                browser.browser
                  .querySelectorAll(".navbar-burger")
                  .foreach(
                    node =>
                      node
                        .addEventListener(
                          "click",
                          menuCallbackBehavior(node, browser),
                        ),
                  )

              },
            ),
      )

}
