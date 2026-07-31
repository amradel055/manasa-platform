
import 'package:flutter/material.dart';

Color? titleColor = Colors.yellow[700];
Color? black = Colors.black87;
Color? yellow = Colors.yellow[700] ;
Color? subTitleColor = Colors.grey[700] ;
Color appBarColor = Colors.black87 ;
Color mainColor = const Color(0xFF477a71);
Color subColor = const Color(0xFF26242f);
Color linkColor = const Color(0xFF11fbb4);
Color cardColor = const Color(0xFFfdffe7);
Color appDrawerColor =const  Color(0xFF373540);
Color appDrawerUpColor =const  Color(0xFFc6cacb);
Color greyColor =const  Color(0xFFf4f4f4);



MaterialStateProperty<Color> buttonColor = MaterialStateProperty.resolveWith<Color>(
        (Set<MaterialState> states) {
     return appBarColor.withOpacity(0.5);
    }) ;
MaterialStateProperty<Color> buttonHomeColor = MaterialStateProperty.resolveWith<Color>(
        (Set<MaterialState> states) {
      return mainColor;
    }) ;
MaterialStateProperty<Color> row1color = MaterialStateProperty.resolveWith<Color>(
        (Set<MaterialState> states) {
      return Colors.white70;
    }) ;
MaterialStateProperty<Color> headrow1color = MaterialStateProperty.resolveWith<Color>(
        (Set<MaterialState> states) {
      return Colors.black54;
    }) ;