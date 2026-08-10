#include "include/flutter_sound/flutter_sound_plugin_c_api.h"

#include <flutter/plugin_registrar_windows.h>

#include "taudio_plugin.h"

void FlutterSoundPluginCApiRegisterWithRegistrar(
    FlutterDesktopPluginRegistrarRef registrar) {
  taudio::TaudioPlugin::RegisterWithRegistrar(
      flutter::PluginRegistrarManager::GetInstance()
          ->GetRegistrar<flutter::PluginRegistrarWindows>(registrar));
}
