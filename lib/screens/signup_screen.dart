import 'package:flutter/material.dart';
import 'package:full_mark/widgets/regular_dark_button.dart';
import '../constants/colors.dart';
import '../constants/helper.dart';
import '../constants/text_styles.dart';
import '../providers/auth_provider.dart';
import '../providers/data_handler_provider.dart';
import '../screens/login_screen.dart';
import '../widgets/drop_down_list.dart';
import '../widgets/loading_widget.dart';
import '../widgets/regular_button.dart';
import '../widgets/text_editor.dart';
import 'package:provider/provider.dart';

import '../widgets/text_editor_email.dart';
import '../widgets/text_editor_singUp.dart';



class SignUp extends StatefulWidget {
  const SignUp({Key? key}) : super(key: key);

  @override
  _SignUpState createState() => _SignUpState();
}

class _SignUpState extends State<SignUp> {
  String? governorateValue;
  String? gradeValue;
  int? selectedGradeId;
  List grade = [] ;
  List _grades = [] ;
  var selectedGovernorate ;
  var userNameController = TextEditingController();
  var firstNameController = TextEditingController();
  var fatherNameController = TextEditingController();
  var grandFatherNameController = TextEditingController();
  var familyNameController = TextEditingController();
  var phoneController = TextEditingController();
  var emailController = TextEditingController();
  var passwordController = TextEditingController();
  var rePasswordController = TextEditingController();
  bool loading = false ;
  List governorateList = [];
  List _governorateList = [];
  List statesList = [];
  List _statesList = [];
  String? statesValue;
  var selectedState ;
  @override
  void initState() {
    // TODO: implement initState
    getGrades();
    context.read<DataHandler>().getCities().then((value){
      governorateList = context.read<DataHandler>().cities ;
      governorateList.forEach((element) {_governorateList.add(element["name"]);});
    });
    super.initState();
  }
  
  getGrades() async{
    await context.read<DataHandler>().getGrades().then((_){
      grade = context.read<DataHandler>().grades ;
    });
    setState(() {
      grade.forEach((element) { _grades.add(element["name"]);});
    });
  }

  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size ;
    loading = context.watch<AuthProvider>().loading ;
    return Scaffold(
      backgroundColor: mainColor,
      appBar: AppBar(
        elevation: 0,

        backgroundColor: mainColor,
        title: const Text("Create an account",style: TextStyle(fontSize: 23),),
        centerTitle: true,
        leading: IconButton(
          icon: const Icon(Icons.arrow_back),
          onPressed: () {
            Navigator.pushReplacement(context, MaterialPageRoute(builder: (context) => const SignIn()));
          },
        ),
      ),
      body: _buildBody(size),
    );
  }

  _buildBody(Size size){
    return Stack(

      children: [
        Container(
          decoration:  BoxDecoration(color:mainColor),
          width: size.width,
          height: size.height,
          child: Container(
            decoration: const BoxDecoration(
                color: Colors.white,
                borderRadius: BorderRadius.only(
                  topLeft: Radius.circular(40),
                  topRight: Radius.circular(40),
                )),

            child:SingleChildScrollView(
              physics: const AlwaysScrollableScrollPhysics(),
              scrollDirection: Axis.vertical,
              child: Padding(
                padding: const EdgeInsets.all(40.0),
                child: SizedBox(
                  child: Column(
                    children:[

                      textEditorSignUp(firstNameController, "First Name" , TextInputType.text , size),
                      textEditorSignUp(fatherNameController, "Father Name" , TextInputType.text, size),
                      textEditorSignUp(grandFatherNameController, "GrandFather Name" , TextInputType.text, size),
                      textEditorSignUp(familyNameController, "Family Name" , TextInputType.text, size),
                      textEditorSignUp(phoneController, "Phone" , TextInputType.phone, size),
                      textEditor(emailController, "E-Mail" , TextInputType.emailAddress, size),
                      appDropDown("Governorate" , size , _governorateList , onChanged , governorateValue),
                      appDropDown("State" , size , _statesList , onStateChanged , statesValue),
                      appDropDown("Grade" , size , _grades , onGardeChanged , gradeValue),
                      textEditorSignUp(userNameController, "User Name To sign In With" , TextInputType.text , size , hintText: "This will be used for login"),
                      textEditorSignUp(passwordController, "Password" , TextInputType.text, size),
                      textEditorSignUp(rePasswordController, "Re-enter Password" , TextInputType.text, size),
                      regularDarkButton(
                          size,
                          mainColor!,
                          "SignUp",
                              () => onPressed(
                              email: emailController.text ,
                              familyName: familyNameController.text ,
                              fatherName: fatherNameController.text ,
                              firstName: firstNameController.text ,
                              grandFatherName: grandFatherNameController.text ,
                              mobile: phoneController.text ,
                              password: passwordController.text ,
                              userName: userNameController.text ,
                                rePassword: rePasswordController.text
                          )
                      ),

                      GestureDetector(
                        child:  Text("Already Have Account ?" , style: TextStyle(color: linkColor , fontSize: 14),) ,
                        onTap: (){
                          Navigator.pushReplacement(context, MaterialPageRoute(builder: (context) => const SignIn()));
                        },
                      )
                    ],
                  ),
                ),
              ),
            ),
          ),
        ),
        loading == true ?
        loadingWidget() :
        const SizedBox(),
      ]

    );
  }
  onPressed(
      {
        String? userName,
      String? firstName,
      String? fatherName,
      String? grandFatherName,
      String? familyName,
      String? mobile,
      String? password,
        String? rePassword,
      String? email}){
    if(
    userName!.isNotEmpty&&
    firstName!.isNotEmpty&&
    fatherName!.isNotEmpty&&
    grandFatherName!.isNotEmpty&&
    familyName!.isNotEmpty&&
    mobile!.isNotEmpty&&
    password!.isNotEmpty&&
    email!.isNotEmpty
    && rePassword!.isNotEmpty
    ){

      if(!email.contains("@")){
        ScaffoldMessenger.of(context).showSnackBar(
            const SnackBar(content: Text("Please Enter a valid email address" , textAlign: TextAlign.center,)
              , backgroundColor: Colors.red,));
      }
      else if (mobile.length != 11){
        ScaffoldMessenger.of(context).showSnackBar(
            const SnackBar(content: Text("Please Enter a valid phone number" , textAlign: TextAlign.center,)
              , backgroundColor: Colors.red,));
      } else if (password != rePassword){
        ScaffoldMessenger.of(context).showSnackBar(
            const SnackBar(content: Text("Passwords aren't match" , textAlign: TextAlign.center,)
              , backgroundColor: Colors.red,));
      }
      else {
        context.read<AuthProvider>().signUp(context ,userName, firstName,
            fatherName, grandFatherName, familyName, mobile, password, email , selectedGradeId , selectedState != null ? selectedState["id"] : 1);

      }

    }
    else {
      ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text("All fields are required" , textAlign: TextAlign.center,),
            backgroundColor: Colors.red,));
    }
  }

 void onChanged(String? newValue) {
    setState(() {
      governorateValue = newValue;
      selectedGovernorate = governorateList[
      governorateList.indexWhere((element) => element["name"] == newValue)];
    });
    print('isssssssssssssss $selectedGovernorate');
    context.read<DataHandler>().getCityState(selectedGovernorate["id"]).then((value){
      setState(() {
        statesList = context.read<DataHandler>().states ;
        statesList.forEach((element) {_statesList.add(element["name"]);});
      });
    });
  }

  void onStateChanged(String? newValue) {
    setState(() {
     statesValue = newValue ;
     selectedState = statesList[statesList.indexWhere((element) => element["name"] == newValue)] ;
    });
  }



  void onGardeChanged(newValue){
    setState(() {
      gradeValue = newValue;
      selectedGradeId = grade[grade.indexWhere((element) => element["name"] == newValue)]["id"];
    });
  }
}


