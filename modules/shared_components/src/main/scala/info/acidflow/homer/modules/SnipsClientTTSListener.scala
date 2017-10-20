package info.acidflow.homer.modules

trait SnipsClientTTSListener {

  def onSayStart(): Unit

  def onSayFinished(): Unit

}
