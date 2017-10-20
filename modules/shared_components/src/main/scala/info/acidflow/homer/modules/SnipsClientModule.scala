package info.acidflow.homer.modules

import info.acidflow.homer.model.NluResult


trait SnipsClientModule {

  def handleIntent(nluResult: NluResult): Unit

  def start(): Unit

  def stop(): Unit = {}

}
