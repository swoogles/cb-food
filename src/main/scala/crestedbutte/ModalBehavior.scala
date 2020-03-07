package crestedbutte

import org.scalajs.dom.raw.MouseEvent
import zio.ZIO

object ModalBehavior {

  def id(value: String) =
    "#" + value

  val addModalOpenBehavior =
    ZIO
      .environment[Browser]
      .map { browser =>
        def activateModal(targetName: String): Unit =
          browser.browser
            .querySelector(targetName)
            .foreach(
              _.classList
                .add("is-active"),
            )

        browser.browser
          .querySelectorAll(".open-arrival-time-modal")
          .foreach { modalOpenButton =>
            modalOpenButton
              .addEventListener(
                "click",
                (clickEvent: MouseEvent) => {
                  val modalContentId =
                    modalOpenButton.attributes
                      .getNamedItem("data-schedule-modal")
                      .value

                  clickEvent.preventDefault();

                  browser.browser
                    .querySelector(
                      id(modalContentId),
                    )
                    .map {
                      modal =>
                        browser.browser
                          .workOnFullHtmlElement(
                            _.classList.add("is-clipped"),
                          )

                        modal
                          .querySelector(".modal-background")
                          .addEventListener(
                            "click",
                            (e: MouseEvent) => {
                              e.preventDefault();
                              removeClippedHtml(browser)
                              modal.classList.remove("is-active");
                            },
                          )

                        activateModal(
                          id(modalContentId),
                        )
                    }
                },
              )
          }
      }

  def removeClippedHtml(browser: Browser) =
    browser.browser.workOnFullHtmlElement(
      _.classList
        .remove("is-clipped"),
    )

  val addModalCloseBehavior =
    ZIO
      .environment[Browser]
      .map(
        browser =>
          browser.browser
            .querySelectorAll(".modal-close")
            .foreach(
              _.addEventListener(
                "click",
                (_: MouseEvent) => {

                  browser.browser.workOnFullHtmlElement(
                    _.classList.remove("is-clipped"),
                  )
                  browser.browser
                    .querySelector(".is-active")
                    .foreach(
                      _.classList
                        .remove("is-active"),
                    )
                },
              ),
            ),
      )

}
