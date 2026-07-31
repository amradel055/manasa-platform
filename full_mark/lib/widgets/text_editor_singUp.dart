import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import '../constants/colors.dart';
import '../constants/text_styles.dart';



Widget textEditorSignUp(TextEditingController controller, String title , TextInputType type , Size size , {hintText}) {
  return Column(
    mainAxisAlignment: MainAxisAlignment.start,
    crossAxisAlignment: CrossAxisAlignment.start,
    children: [
      // Text(
      //   title,
      //   style: subTitleUpStyle,
      // ),
      const SizedBox(
        height: 7,
      ),
      Container(
        decoration: BoxDecoration(
          borderRadius: BorderRadius.all(Radius.circular(10)),
          border: Border.all(color: appBarColor)
        ),
        child: TextField(

          controller: controller,
          inputFormatters: <TextInputFormatter>[
            FilteringTextInputFormatter.allow(RegExp('[a-z A-Z 0-9 ]'))
          ],
          obscureText: (title ==  "Password" || title ==  "Re-enter Password") ? true : false,
          keyboardType: type,
          decoration: InputDecoration(
            hintText: title ,
              contentPadding: EdgeInsets.all(10.0),
              focusedBorder: InputBorder.none,
            disabledBorder:  InputBorder.none,
            border: InputBorder.none,
            hintStyle:  TextStyle(
              fontSize: 15 ,
              color: appBarColor
            )
          ),
        ),
      ),
      const SizedBox(
        height: 17,
      ),
    ],
  );
}
