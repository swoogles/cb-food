package crestedbutte

import org.scalajs.dom.raw.{MouseEvent, NamedNodeMap, NodeList}
import zio.ZIO

object ModalBehavior {

  val addModalOpenBehavior =
    ZIO
      .environment[Browser]
      .map { browser =>
        def activateModal(targetName: String): Unit =
          org.scalajs.dom.document.body // TODO from environment.
            .querySelector(targetName)
            .classList
            .add("is-active")

        // TODO function that iterates over elements when passed a selector
        val modalOpenButtons: NodeList = browser.browser
          .window()
          .document
          .querySelectorAll(".open-arrival-time-modal")

        for { i <- Range(0, modalOpenButtons.length) } {
          modalOpenButtons
            .item(i)
            .addEventListener(
              "click",
              (clickEvent: MouseEvent) => {
                //                clickEvent.target.
                val attributesOfClickedItem: NamedNodeMap =
                  modalOpenButtons
                    .item(i)
                    .attributes

                val modalContentId =
                  modalOpenButtons
                    .item(i)
                    .attributes
                    .getNamedItem("data-schedule-modal")
                    .value

                clickEvent.preventDefault();

                val modal = org.scalajs.dom.document.body
                  .querySelector(
                    "#" + modalContentId
                  )
                val html =
                  org.scalajs.dom.document
                    .querySelector("html")
                //          modal.classList.add("is-active");
                html.classList.add("is-clipped");

                modal
                  .querySelector(".modal-close")
                  .addEventListener(
                    "click",
                    (e: MouseEvent) => {
                      e.preventDefault();

                      org.scalajs.dom.document
                        .querySelector("html")
                        .classList
                        .remove("is-clipped");
                    }
                  )

                modal
                  .querySelector(".modal-background")
                  .addEventListener(
                    "click",
                    (e: MouseEvent) => {
                      e.preventDefault();

                      org.scalajs.dom.document
                        .querySelector("html")
                        .classList
                        .remove("is-clipped");

                      modal.classList.remove("is-active");
                    }
                  );

                activateModal(
                  "#" + modalContentId
                )
              }
            )
        }
      }

  val addModalCloseBehavior =
    ZIO
      .environment[Browser]
      .map { browser =>
        val modalCloseButtons = browser.browser
          .window()
          .document
          .querySelectorAll(".modal-close")
        for { i <- Range(0, modalCloseButtons.length) } {
          modalCloseButtons
            .item(i)
            .addEventListener("click",
                              (mouseEvent: MouseEvent) =>
                                browser.browser
                                  .window()
                                  .document
                                  .querySelector(".is-active")
                                  .classList
                                  .remove("is-active"))

        }
      }

}
