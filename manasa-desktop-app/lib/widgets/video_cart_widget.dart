
import 'package:flutter/material.dart';

import 'package:intl/intl.dart';
import '../constants/text_styles.dart';



Widget videoCartWidget (String url,Size size ,String image ,BuildContext context){
  return  GestureDetector(onTap: (){
    // Navigator.push(context,
    // MaterialPageRoute(builder: (context) => PdfViewerPage()));
  },
    child: Padding(
      padding: const EdgeInsets.fromLTRB(0,0,10,0),
      child: Container(
        width: size.width * .15,
        height: size.height * .15,
        decoration:const BoxDecoration(
          borderRadius: BorderRadius.all(Radius.circular(
              10.0)),

        ),

        child: Column(
          children: [
            Container(
              height: size.height * 0.15,
              width: size.width * 0.25,
              decoration:  BoxDecoration(
                  borderRadius: const BorderRadius.all(Radius.circular(15)),
                  image: DecorationImage(
                      fit: BoxFit.cover,
                      image: NetworkImage(
                          image==""?"https://www.vidyard.com/wp-content/uploads/Screen-Shot-2014-08-08-at-8.48.40-AM.png":image)
                  )
              ),
            ),


          ],
        ),
      ),
    ),
  );
}