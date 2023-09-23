import 'package:flutter/material.dart';
import '../constants/text_styles.dart';


Widget regularButton(Size size , Color color , String text , VoidCallback onPressed ){
  return Padding(
    padding: const EdgeInsets.all(8.0),
    child: SizedBox(
      width: size.width * 0.4,
      child: ElevatedButton(
          onPressed:onPressed,
          style: ButtonStyle(
              backgroundColor: MaterialStateProperty.resolveWith<Color>(
                  (Set<MaterialState> states) {
                return color.withOpacity(1.0);
              }) ,

          ),
          child: Center(
            child: SizedBox(height: size.height *0.04 ,child: FittedBox(child: Text(text , style: buttonsTextStyle,))),
          )

      ),
    ),
  );
}