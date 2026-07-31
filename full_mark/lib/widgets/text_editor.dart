import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import '../constants/colors.dart';
import '../constants/text_styles.dart';



Widget textEditor(TextEditingController controller, String title , TextInputType type , Size size , {hintText}) {
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
          border: Border.all(color: appBarColor),
          color: Colors.white
        ),
        child: TextField(
          controller: controller,
          style:  TextStyle(fontWeight: FontWeight.w500 , color: Colors.black),
          // inputFormatters: <TextInputFormatter>[
          //   FilteringTextInputFormatter.allow(RegExp('[a-z A-Z 0-9 ]'))
          // ],
          obscureText: title ==  "Password" ? true : false,
          keyboardType: type,
          decoration: InputDecoration(
            hintText: title ,
            contentPadding: EdgeInsets.all(10),
            focusedBorder: InputBorder.none,
            disabledBorder:  InputBorder.none,
            border: InputBorder.none,
            hintStyle:  TextStyle(
              fontSize: 20 ,
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
