import 'package:desktop_screen/constants/colors.dart';
import 'package:pod_player/pod_player.dart';
import 'package:flutter/material.dart';
import 'package:webview_windows/webview_windows.dart';

final _navigatorKey = GlobalKey<NavigatorState>();
class PlayVideoFromVimeo extends StatefulWidget {
  const PlayVideoFromVimeo({Key? key, required this.url}) : super(key: key);
  final String url;

  @override
  State<PlayVideoFromVimeo> createState() => _PlayVideoFromVimeoState();
}

class _PlayVideoFromVimeoState extends State<PlayVideoFromVimeo> {

  final _controller = WebviewController();

  @override
  void initState() {
    super.initState();
    initializeController();
  }

  initializeController() async {
    await _controller.initialize();
    await _controller.loadStringContent("""
      <iframe id='myVideo'
      src="${widget.url}" 
      frameborder="0"
      width='100%'
      height='100%'
      allow="autoplay; fullscreen" 
      style="position:absolute;top:0;left:0;width:100%;height:100%;" 
      ></iframe>
      """);
    setState(() {});
  }

  @override
  dispose(){
    _controller.dispose();
    super.dispose();
  }

  Future<WebviewPermissionDecision> _onPermissionRequested(String url, WebviewPermissionKind kind, bool isUserInitiated) async {
    final decision = await showDialog<WebviewPermissionDecision>(
      context: _navigatorKey.currentContext!,
      builder: (BuildContext context) => AlertDialog(
        title: const Text('WebView permission requested'),
        content: Text('WebView has requested permission \'$kind\''),
        actions: <Widget>[
          TextButton(
            onPressed: () => Navigator.pop(context, WebviewPermissionDecision.deny),
            child: const Text('Deny'),
          ),
          TextButton(
            onPressed: () => Navigator.pop(context, WebviewPermissionDecision.allow),
            child: const Text('Allow'),
          ),
        ],
      ),
    );

    return decision ?? WebviewPermissionDecision.none;
  }

  @override
  Widget build(BuildContext context) {
    if(_controller.value.isInitialized) {
      _controller.executeScript("""
      var video = document.getElementById("myVideo");
  video.width = 100%;
  video.height = 100%;
  
""");
    }
    return Scaffold(
      appBar: AppBar(
        elevation: 0,

        backgroundColor: Colors.white,
        title: Text(""),

        centerTitle: true,
        leading: IconButton(
          icon: Icon(
            Icons.arrow_back,
            color: mainColor,
          ),
          onPressed: () {
            Navigator.pop(context);
          },
        ),
      ),
      body: Webview(
        _controller,
        permissionRequested: _onPermissionRequested,
      ),
    );
  }
}
