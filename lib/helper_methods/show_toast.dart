import 'package:flutter/material.dart';

showToast(BuildContext context, String message){
  final snackBar = SnackBar(
    content: Text(message,textAlign: TextAlign.center),
    behavior: SnackBarBehavior.floating,
    margin: const EdgeInsets.all(25.0),
    shape: RoundedRectangleBorder(
      borderRadius: BorderRadius.circular(25.0)
    ),
  );
  ScaffoldMessenger.of(context).showSnackBar(snackBar);
}