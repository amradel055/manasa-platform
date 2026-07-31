

import 'dart:async';

import 'package:flutter/material.dart';
import 'package:full_mark/constants/colors.dart';
import 'package:provider/provider.dart';
import 'package:full_mark/providers/auth_provider.dart';
import 'package:full_mark/screens/login_screen.dart';

import 'home_screen.dart';

class SplashScreen extends StatefulWidget {
  const SplashScreen({Key? key}) : super(key: key);

  @override
  _SplashScreenState createState() => _SplashScreenState();
}

class _SplashScreenState extends State<SplashScreen> with SingleTickerProviderStateMixin{
  late AnimationController controller;
  late Animation<double> animation;
  late Timer timer ;
  @override
  void initState() {
    // TODO: implement initState
    controller = AnimationController(
        duration: const Duration(milliseconds: 600), vsync: this);
    animation =  Tween<double>(begin: -1, end : 1).animate(controller);
    controller.forward();
    timer  = Timer(const Duration(seconds: 5), (){
      getRoad ();
    });
    timer;
    super.initState();
  }



  getRoad (){
     context.read<AuthProvider>().getData().then((student){
      if(student != null && student.success != null ){

        Navigator.pushReplacement(context, MaterialPageRoute(builder: (context) => const HomeScreen()));
      }
      else {
        Navigator.pushReplacement(context, MaterialPageRoute(builder: (context) => const SignIn()));

      }
    });
  }
  @override
  void dispose() {
    // TODO: implement dispose
    timer.cancel() ;
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size ;
    return Container(
      height: size.height,
      width: size.width,
      color: mainColor,
      child: Column(
        mainAxisAlignment: MainAxisAlignment.spaceEvenly,
        children: [
          FadeTransition(
            opacity: animation,
            child: SizedBox(
              child: Image(
                image: const AssetImage("assets/images/logo1.png"),
                height: size.height * 0.6,
                width: size.width,
              )
            ),
          ),
          CircularProgressIndicator(
            backgroundColor: Colors.yellow[700],
            color: Colors.grey,
          )
        ],
      ),
    );
  }
}
