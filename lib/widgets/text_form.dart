import 'package:flutter/material.dart';

class TextFormFieldWithBorder extends StatelessWidget {
  const TextFormFieldWithBorder({

    required this.obseecure,
    required this.hintText,
    this.width,
  });
  final bool obseecure;
  final String hintText;
  final double? width;
  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.only(right: 5),
      child: SizedBox(
        width: width ?? MediaQuery.of(context).size.width - 20,
        child: TextFormField(
          obscureText: obseecure,
          decoration: InputDecoration(
              hintText: hintText,
              border: OutlineInputBorder(
                  borderSide: const BorderSide(color: Colors.white, width: 2.0),
                  borderRadius: BorderRadius.circular(15))),
        ),
      ),
    );
  }
}
