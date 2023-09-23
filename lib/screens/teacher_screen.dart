import 'package:flutter/material.dart';
import 'package:full_mark/models/teacher_model.dart';
import 'package:full_mark/screens/activate_request_screen.dart';
import 'package:full_mark/widgets/image_widget.dart';
import 'package:full_mark/widgets/regular_dark_button.dart';
import '../constants/colors.dart';
import '../constants/strings.dart';
import '../constants/text_styles.dart';
import '../widgets/app_drawer.dart';



class TeacherScreen extends StatefulWidget {
  const TeacherScreen(this.teacher ,{Key? key}) : super(key: key);
  final TeacherModel teacher;

  @override
  _TeacherScreenState createState() => _TeacherScreenState();
}

class _TeacherScreenState extends State<TeacherScreen> {


  final GlobalKey<ScaffoldState> _scaffoldKey =  GlobalKey<ScaffoldState>();

  @override
  void initState() {
    super.initState();

  }

  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size ;
    return Scaffold(
      backgroundColor: mainColor,
      key: _scaffoldKey,
      drawer: const DrawerWidget(),
      appBar:AppBar(
        backgroundColor: mainColor,
        title: Text(widget.teacher.name??"" , style: titleTextStyle,),
        centerTitle: true,
        elevation: 0,
        leading: IconButton(
          onPressed: (){
            Navigator.pop(context);
          },
          icon: Icon(Icons.arrow_back , color: Colors.white,),
        ),

        actions: [
          IconButton(
            onPressed: (){
              _scaffoldKey.currentState!.openDrawer();
            },
            icon: Icon(Icons.menu , color: Colors.white,),
          )
        ],
      ),
      body: Stack(
        children: [
          Padding(
            padding: const EdgeInsets.all(0.0),
            child: Container(
              decoration:  BoxDecoration(
                  color:mainColor,
                  borderRadius: BorderRadius.only(
                    bottomLeft: Radius.circular(100),
                    bottomRight: Radius.circular(100),
                  )),

              height: size.height * 0.4,
              width: size.width,
              child: ImageWidget(path:widget.teacher.image !=  null? (uploadUrl + widget.teacher.image!):"https://tekno-class.com/new-lms/wp-content/uploads/2021/09/teacher.png" , fit: BoxFit.fill),
            ),
          ),
          Align(
            alignment: AlignmentDirectional.bottomCenter,
            child: Padding(
              padding: const EdgeInsets.all(18.0),
              child: Container(
                width: size.width*.9,
                height: size.height*.5,
                decoration: const BoxDecoration(
                    color: Colors.white,
                    borderRadius: BorderRadius.all(Radius.circular(20))
                ),
                child: SingleChildScrollView(
                  padding: const EdgeInsets.all(8.0),
                  child: Column(
                    children: [
                      SizedBox(
                          height: size.height*.4,
                          child: Text(widget.teacher.description ??'',textAlign: TextAlign.left , style:  TextStyle(fontWeight: FontWeight.w500, fontSize: 17 , color: mainColor ,fontFamily: "Messiri"),)),
                      Align(
                        alignment: AlignmentDirectional.bottomCenter,
                        child: regularDarkButton(
                            size,
                            mainColor!,
                            "Book Now",
                                () =>  Navigator.push(context, MaterialPageRoute(builder: (_)=>const ActivateRequestScreen()))

                        ),
                      ),
                    ],
                  ),
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }
}
