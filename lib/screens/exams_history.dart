import 'package:flutter/material.dart';
import 'package:flutter/material.dart';
import '../constants/colors.dart';
import '../constants/text_styles.dart';
import '../models/exam_model.dart';
import '../providers/data_handler_provider.dart';
import '../screens/solved_exam_screen.dart';
import 'package:provider/provider.dart';

class ExamsHistory extends StatefulWidget {
  const ExamsHistory({Key? key}) : super(key: key);

  @override
  _ExamsHistoryState createState() => _ExamsHistoryState();
}

class _ExamsHistoryState extends State<ExamsHistory> {
  late List<Exam> previousExams = [];

  final _scaffoldKey = GlobalKey<ScaffoldState>();

  @override
  void initState() {
    WidgetsBinding.instance.addPostFrameCallback((_) async {
      bool connected = context.read<DataHandler>().connected ;
      if(connected == true){
      await getData();
      }
      else {
        context.read<DataHandler>().getLocalExams();
      }



    });
    // TODO: implement initState
    super.initState();
  }

  getData() async {
    await context.read<DataHandler>().getPreviousExams().then((value) {
      previousExams = context.read<DataHandler>().previousExams;

    });
  }

  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size;
    previousExams = context.watch<DataHandler>().previousExams;
    return Scaffold(
      key: _scaffoldKey,
      appBar: AppBar(
        backgroundColor: appBarColor,
        title: Text(
          "Previous Exams",
          // style: s,
        ),
        centerTitle: true,
        leading: IconButton(
          onPressed: (){
            Navigator.pop(context);
          },
          icon: Icon(Icons.arrow_back , color: yellow,),
        ),
        actions: [
          IconButton(
            onPressed: (){
              _scaffoldKey.currentState!.openDrawer();
            },
            icon: Icon(Icons.menu , color: yellow,),
          )
        ],
      ),
      body: _buildBody(size),
    );
  }

  Widget _buildBody(Size size) {
    return Container(
      height: size.height,
      child: SingleChildScrollView(
        child: Container(
          height: size.height * 0.86,
          child: ListView(
            children: previousExams.map((Exam exam) {
              return GestureDetector(
                onTap: () {
                  Navigator.push(
                      context,
                      MaterialPageRoute(
                          builder: (context) => SolvedExam(exam)));
                },
                child: Card(
                  elevation: 8.0,
                  color: Colors.grey,
                  child: SizedBox(
                      child: ListTile(
                    title: Center(
                        child: Row(
                      mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                      children: [
                        Text(
                          exam.examName!,
                          style: const TextStyle(
                              color: Colors.black,
                              fontSize: 22,
                              fontWeight: FontWeight.bold),
                        ),
                        // Text("${exam.score} / ${exam.degree}"),
                      ],
                    )),
                    subtitle: Text(
                      "${exam.score} / ${exam.degree}",
                      style: titleTextStyle,
                    ),
                  )),
                ),
              );
            }).toList(),
          ),
        ),
      ),
    );
  }
}
