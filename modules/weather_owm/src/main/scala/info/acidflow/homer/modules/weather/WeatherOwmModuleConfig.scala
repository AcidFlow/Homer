package info.acidflow.homer.modules.weather

case class WeatherOwmModuleConfig(
  owmBaseUrl: String,
  owmApiKey: String,
  owmUnits: String,
  owmDefaultCityId: String)
