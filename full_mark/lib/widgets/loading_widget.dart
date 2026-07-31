import 'package:flutter/material.dart';
import '../constants/colors.dart';
import '../constants/text_styles.dart';

Widget loadingWidget () {
  return
    Center(
      child: Container(
          decoration: BoxDecoration(
              color: appBarColor.withOpacity(0.5) ,
              borderRadius: BorderRadius.circular(5)
          ),
          child:  Padding(
            padding: const EdgeInsets.all(10),
            child: Column(
              mainAxisAlignment:MainAxisAlignment.center ,
              mainAxisSize: MainAxisSize.min,
              children: [
                const  CircularProgressIndicator(),
                Padding(
                  padding: const EdgeInsets.all(5.0),
                  child: Text("Loading....", style: subTitleStyle,),
                )
              ],
            ),
          )),
    );
}