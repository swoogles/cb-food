package crestedbutte

sealed trait ExternalAction

case class CallLocation(phoneNumber: PhoneNumber)
    extends ExternalAction

case class VisitHomePage(website: Website) extends ExternalAction

case class VisitFacebookPage(website: Website) extends ExternalAction

case class ExternalActionCollection(primary: ExternalAction,
                                    others: Seq[ExternalAction])
