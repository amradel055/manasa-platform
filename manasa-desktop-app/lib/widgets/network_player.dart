import 'package:desktop_screen/constants/colors.dart';
import 'package:flutter/material.dart';
import 'package:webview_windows/webview_windows.dart';

final _navigatorKey = GlobalKey<NavigatorState>();

class PlayVideoFromNetwork extends StatefulWidget {
  const PlayVideoFromNetwork({Key? key,required this.url}) : super(key: key);
  final String url;

  @override
  State<PlayVideoFromNetwork> createState() => _PlayVideoFromNetworkState();
}

class _PlayVideoFromNetworkState extends State<PlayVideoFromNetwork> {
  final _controller = WebviewController();

  double get width => MediaQuery.of(context).size.width * .95;
  double get height => MediaQuery.of(context).size.height * .85;

  @override
  void initState() {
    super.initState();
    initializeController();
  }

  initializeController() async {
    await _controller.initialize();
    await _controller.loadStringContent("""
      <video id="myVideo" width='100%' height='100%' controls>
  <source src="${widget.url}" type="video/mp4">
Your browser does not support the video tag.
</video>
      """);
    setState(() {});
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
  dispose(){
    _controller.dispose();
    super.dispose();
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
