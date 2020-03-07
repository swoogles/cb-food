package crestedbutte

case class ComponentData(
  namedRoute: NamedRoute
) {
  val componentName = namedRoute.routeName.name
}
