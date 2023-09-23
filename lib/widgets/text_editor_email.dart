import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import '../constants/colors.dart';
import '../constants/text_styles.dart';



Widget textEditorEmail(TextEditingController controller, String title , TextInputType type , Size size , {hintText}) {
  return Column(
    mainAxisAlignment: MainAxisAlignment.start,
    crossAxisAlignment: CrossAxisAlignment.start,
    children: [
      Text(
        title,
        style: subTitleStyle,
      ),
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
            FilteringTextInputFormatter.allow(RegExp(r'^[^@]+@[^@]+\.[^@]+'))
          ],
          obscureText: title ==  "Password" ? true : false,
          keyboardType: type,
          decoration: InputDecoration(
            hintText: hintText ,

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
