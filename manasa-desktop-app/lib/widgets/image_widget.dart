import 'dart:io';
import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';

import 'image_preview.dart';

class ImageWidget extends StatelessWidget {

  const ImageWidget({
    Key? key,
    required this.path,
    this.tag,
    this.fit = BoxFit.contain,
    this.keepOriginalColor,
    this.color,
    this.backgroundColor,
    this.width,
    this.height,
    this.border,
    this.onPressed,
    this.isCircle = false,
    this.disablePreview = false,
    this.radius = 10,
    this.isPadding=true,
    this.isCenter=true
  }) :super(key: key);

  final String path;
  final dynamic tag;
  final Color? color;
  final Color? backgroundColor;
  final bool? keepOriginalColor;
  final BoxFit fit;
  final double? width;
  final double? height;
  final bool isCircle;
  final bool isPadding;
  final bool isCenter;
  final Border? border;
  final bool disablePreview;
  final Function? onPressed;
  final double radius;

  @override
  Widget build(BuildContext context) {
    final heroTag = tag ?? UniqueKey();
    Widget? child;
    if(path.contains("http")) {
      child = CachedNetworkImage(imageUrl: path,fit: fit,
        height: height,width: width,color: color,
        placeholder:  (_,__){
          return LayoutBuilder(
              builder: (context,c) {
                return Padding(
                  padding: isPadding ? EdgeInsets.only(bottom: isCenter  ? 0 : 70):EdgeInsets.zero,
                  child: const Center(child: CircularProgressIndicator()),
                );
              }
          );
        },
        errorWidget: (_,error,___){
          return LayoutBuilder(
            builder: (context,c) {
              return Padding(
                padding: isPadding ? EdgeInsets.only(bottom: isCenter  ? 0 : 70):EdgeInsets.zero,
                child: Center(child: Text(error,textAlign: TextAlign.center)),
              );
            }
          );
        },
      );
    } else if(path.split(".").last == "svg"){
      child = SvgPicture.asset(path,color: color,width: width,height: height,fit: fit,);
    } else if(path.startsWith("asset")){
      child = Image.asset(path, color: color,width: width,height: height,fit: fit,);
    } else {
      child = Image.file(File(path), color: color,width: width,height: height,fit: fit,);
    }
    return GestureDetector(
      onTap: (disablePreview && onPressed == null)?null:(){
        if(onPressed != null) {
          onPressed!();
        } else {
          Navigator.push(context, MaterialPageRoute(builder: (_) => ImagePreviewWidget(path: path,tag: heroTag)));
        }
      },
      child: Container(
        width: width,
        height: height,
        decoration: BoxDecoration(
            shape: isCircle?BoxShape.circle:BoxShape.rectangle,
            border: border,
            borderRadius: isCircle?null:BorderRadius.all(Radius.circular(radius)),
            color: backgroundColor
        ),
        foregroundDecoration: BoxDecoration(
          shape: isCircle?BoxShape.circle:BoxShape.rectangle,
          border: border,
          borderRadius: isCircle?null:BorderRadius.all(Radius.circular(radius)),
        ),
        clipBehavior: Clip.antiAlias,
        child: Hero(tag: heroTag,child: child),
      ),
    );
  }
}