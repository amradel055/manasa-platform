import 'package:flutter/material.dart';
import 'package:webview_windows/webview_windows.dart';
import 'package:youtube_player_flutter/youtube_player_flutter.dart';

import '../constants/colors.dart';

final _navigatorKey = GlobalKey<NavigatorState>();

class PlayVideoFromYoutube extends StatefulWidget {
  const PlayVideoFromYoutube({Key? key, required this.url}) : super(key: key);
  final String url;

  @override
  State<PlayVideoFromYoutube> createState() => _PlayVideoFromYoutubeState();
}

final staticAnchorKey = GlobalKey();

class _PlayVideoFromYoutubeState extends State<PlayVideoFromYoutube> {

  final _controller = WebviewController();

  @override
  void initState() {
    super.initState();
    initializeController();
  }

  initializeController() async {
    await _controller.initialize();
    await _controller.loadStringContent("""
      <iframe width="560" height="315" 
      src="${widget.url}" 
      title="YouTube video player" 
      frameborder="0" 
      allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" 
      allowfullscreen></iframe>
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
  Widget build(BuildContext context) {
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
