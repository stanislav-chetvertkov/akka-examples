package typed

package object _1_example {

  type TypedActorRef[T] = de.knutwalker.akka.typed.ActorRef[T]
  type TProps[T] = de.knutwalker.akka.typed.Props[T]
  type UntypedActorRef = akka.actor.ActorRef

}
