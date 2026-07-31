import 'package:desktop_screen/widgets/loading_widget.dart';
import 'package:desktop_screen/widgets/regular_button.dart';
import 'package:desktop_screen/widgets/text_editor.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/painting.dart';
import 'package:flutter/rendering.dart';
import 'package:flutter/services.dart';
import 'package:screenshot/screenshot.dart';
import '../constants/colors.dart';
import '../constants/text_styles.dart';
import '../providers/auth_provider.dart';

import 'package:provider/provider.dart';


class SignIn extends StatefulWidget {
  const SignIn({Key? key}) : super(key: key);

  @override
  _SignInState createState() => _SignInState();
}

class _SignInState extends State<SignIn> {

  var userNameController = TextEditingController();
  var passwordController = TextEditingController();
  bool loading = false ;
  ScreenshotController screenshotController = ScreenshotController();




  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size ;
    loading = context.watch<AuthProvider>().loading;
    return
      // controller: screenshotController,
      Scaffold(
        backgroundColor: mainColor,
        appBar: AppBar(
          // centerTitle: true,
          leading:  Center(
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Text(
                          'LUMEN',
                          style: TextStyle(
                              color: Color(0xff05fcb5),
                              fontSize: 35,
                              fontWeight: FontWeight.bold,
                              fontFamily: "lobster"),
                        ),
            Padding(
                        padding: EdgeInsets.only(left: 35.0),
                        child: Text(
                          'easy education',
                          style: TextStyle(
                              color: Colors.white,
                              fontSize: 15,
                              fontWeight: FontWeight.bold,
                             ),
                        ),
                      ),
              ],
            ),
          ),
          leadingWidth: size.width*.55,
          shape:  RoundedRectangleBorder(
          borderRadius: BorderRadius.vertical(
          bottom:  Radius.elliptical(size.width * 0.5, 30),
                  ),
                ),
          // RoundedRectangleBorder(borderRadius: BorderRadius.only(bottomLeft: Radius.circular(size.width *0.4) , bottomRight: Radius.circular(size.width *0.4))) ,
          toolbarHeight: size.height * 0.36,
          backgroundColor: subColor,
          title:SizedBox(child: Image.asset("assets/images/studen.png" ,fit: BoxFit.cover),)
           ),
        body: _buildBody(size),


    );
  }

  _buildBody(Size size){
    return
      Screenshot(
        controller: screenshotController,
         child:Container(
        color: mainColor,
        child: ListView(
          scrollDirection: Axis.vertical,
          children: [

            Center(
              child: SizedBox(
                width: size.width *.4,
                child: Stack(
                  children: [
                     Padding(
                       padding: const EdgeInsets.all(20.0),
                       child: SizedBox(
                        width: size.width,
                        // height: size.height,
                        child:SingleChildScrollView(
                          physics: const AlwaysScrollableScrollPhysics(),
                          child: Column(
                            mainAxisAlignment: MainAxisAlignment.center,
                            children:[

                              Padding(
                                padding: const EdgeInsets.all(30.0),
                                child: Column(
                                  children: [
                                    textEditor(userNameController, "User Name" , TextInputType.text, size),
                                    textEditor( passwordController, "Password" , TextInputType.text, size),
                                    // PasswordField(
                                    //   // fieldKey: _passwordFieldKey,
                                    //   helperText: 'No more than 8 characters.',
                                    //   labelText: 'Password *',
                                    //   onFieldSubmitted: (String value) {
                                    //     setState(() {
                                    //       passwordController.text = value;
                                    //     });
                                    //   },
                                    // ),

                                    SizedBox(
                                      height:  size.height * 0.02,),
                                    regularButton(size, Colors.white!, "Login In", () => onPressed(userName: userNameController.text , password: passwordController.text)),
                                    SizedBox(
                                      height:  size.height * 0.01,),
                                    Column(
                                      children: [
                                        Padding(
                                          padding: const EdgeInsets.fromLTRB(0,0,0,20),
                                          child: Align(
                                              alignment : Alignment.center,
                                              child: GestureDetector(
                                                child: const Text("Forget Password ?",style: TextStyle(color: Colors.white) , textAlign: TextAlign.end,),
                                                onTap: (){},
                                              )),
                                        ),
                                        // Row(
                                        //   mainAxisAlignment: MainAxisAlignment.center,
                                        //   children: [
                                        //     const Text("Don't have account ?", style: TextStyle(
                                        //     color: Colors.white),),
                                        //     SizedBox(
                                        //         width: size.width * 0.03),
                                        //     GestureDetector(
                                        //       child: const Text("Sign Up Now",
                                        //         style: TextStyle(
                                        //
                                        //             color: Color(0xFF11fbb4)),),
                                        //       onTap: (){
                                        //         // Navigator.pushReplacement(context, MaterialPageRoute(builder: (context) => const SignUp()));
                                        //       },
                                        //     )
                                        //   ],
                                        // ),
                                      ],
                                    )
                                  ],
                                ),
                              ),
                         // ElevatedButton(
                         //   child: Text(
                         //     'Capture Above Widget',
                         //   ),
                         //   onPressed: () {
                         //     screenshotController
                         //         .capture(delay: Duration(milliseconds: 10))
                         //         .then((capturedImage) async {
                         //       ShowCapturedWidget(context, capturedImage!);
                         //     }).catchError((onError) {
                         //       print(onError);
                         //     });
                         //   },
                         // ),
                         ElevatedButton(
                           onPressed: () {
                             var container = Container(
                                 padding: const EdgeInsets.all(30.0),
                                 decoration: BoxDecoration(
                                   border: Border.all(color: Colors.blueAccent, width: 5.0),
                                   color: Colors.redAccent,
                                 ),
                                 child: Stack(
                                   children: [
                                     Image.asset(
                                       'assets/images/logo.png',
                                     ),
                                     Text(
                                       "This is an invisible widget",
                                       style: Theme.of(context).textTheme.titleLarge,
                                     ),
                                   ],
                                 ));
                             screenshotController
                                 .captureFromWidget(
                                 InheritedTheme.captureAll(
                                     context, Material(child: container)),
                                 delay: Duration(seconds: 1))
                                 .then((capturedImage) {
                               ShowCapturedWidget(context, capturedImage);
                             });
                           },
                           child: Text(
                             'Capture An Invisible Widget',
                           ),


                         )   ],
                          ),
                        ),
                    ),
                     ),
                    loading == true ?
                    loadingWidget() :
                    const SizedBox(),
                  ],
                ),
              ),
            ),
          ],
        ),
    ),
      );
  }
  Future<dynamic> ShowCapturedWidget(
      BuildContext context, Uint8List capturedImage) {
    return showDialog(
      useSafeArea: false,
      context: context,
      builder: (context) => Scaffold(
        appBar: AppBar(
          title: Text("Captured widget screenshot"),
        ),
        body: Center(child: Image.memory(capturedImage)),
      ),
    );
  }
  onPressed({String? userName, String? password}){
    if(userName!.isNotEmpty && password!.isNotEmpty) {
      context.read<AuthProvider>().signIn(context, userName, password);
    }
    else {
      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text("All fields are required" , textAlign: TextAlign.center,) , backgroundColor: Colors.red,));
    }
  }
}
