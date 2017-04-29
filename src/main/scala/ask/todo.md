todo: how ask works

do not close over sender

```
def receive ={
case s:String => {

  val f = future{
      val ans = search(s)
      println("Input Request: "+s+" output:"+ans+" "+sender.path)
  }
  f.onComplete{
    case Success(x) => sender ! x
    case Failure(y) => println("Could not complete it")
  } 

}
```

You are making a very common mistake of "closing over mutable state". The closure you pass to onComplete does not make a copy of this.sender, so when your onComplete gets called, you are sending the message to whatever this.sender happens to point to at that time, not what it pointed to when you created the closure.

You can avoid this problem by creating your own local, immutable copy of the current contents of this.sender, and reference that value in the closure:

```
val origSender = sender
f.onComplete {
    case Successs(x) => origSender ! x
    ...
}
```



Pipe pattern

`import akka.pattern.pipe`


```
val reply = sender
future {
  val ans = searchAndCache(s)
  println("Input Request: "+s+" output:"+ans+" "+reply.path)
  ans
} pipeTo reply
```


show that actors block on the future