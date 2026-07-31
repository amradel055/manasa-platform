import 'dart:io';

import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/material.dart';
import 'package:photo_view/photo_view.dart';

class ImagePreviewWidget extends StatelessWidget {
  const ImagePreviewWidget({Key? key,required this.path, this.tag}) : super(key: key);

  final String path;
  final dynamic tag;

  @override
  Widget build(BuildContext context) {
    late ImageProvider image;
    if(path.contains("http")){
      image = CachedNetworkImageProvider(path);
    } else if(path.contains("asset")){
      image = AssetImage(path);
    } else {
      image = FileImage(File(path));
    }
    return SafeArea(
      child: Scaffold(
        extendBodyBehindAppBar: true,
        appBar: AppBar(
          backgroundColor: Colors.transparent,
        ),
        body: Container(
            color: Colors.black,
            child: PhotoView(
              heroAttributes: PhotoViewHeroAttributes(
                tag: tag ?? UniqueKey()
              ),
              loadingBuilder: (context, event) => Center(
                child: SizedBox(
                  width: 20.0,
                  height: 20.0,
                  child: CircularProgressIndicator(
                    value: event == null
                        ? 0.0
                        : event.cumulativeBytesLoaded.toDouble() / (event.expectedTotalBytes??0),
                  ),
                ),
              ),
              imageProvider: image,
            )
        ),
      ),
    );
  }
}
