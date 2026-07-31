
import 'package:desktop_screen/widgets/pdf_view.dart';
import 'package:flutter/material.dart';

import 'package:intl/intl.dart';
import '../constants/text_styles.dart';



Widget AttachWidget (String url,Size size ,String image ,BuildContext context){
  return  GestureDetector(onTap: (){
    Navigator.push(context,
        MaterialPageRoute(builder: (context) => PdfViewerPage(url: url,)));
  },
    child: Padding(
      padding: const EdgeInsets.fromLTRB(0,0,10,0),
      child: Container(
        width: size.width * .25,
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
                          image==""?"https://www.dinamize.com.br/wp-content/uploads/2016/07/dblogx-2.gif":image)
                  )
              ),
            ),


          ],
        ),
      ),
    ),
  );
}