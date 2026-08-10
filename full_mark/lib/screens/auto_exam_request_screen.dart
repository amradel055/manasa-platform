import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:full_mark/models/exam_model.dart';
import 'package:full_mark/models/material_model.dart';
import 'package:full_mark/models/period_model.dart';
import 'package:full_mark/providers/screen_providers/activate_request_provider.dart';
import 'package:full_mark/screens/do_auto_exam_screen.dart';
import 'package:full_mark/widgets/app_loading_overlay.dart';
import 'package:full_mark/widgets/loading_widget.dart';
import '../constants/colors.dart';
import '../constants/strings.dart';
import '../constants/text_styles.dart';
import '../models/lesson_model.dart';
import '../providers/data_handler_provider.dart';
import '../screens/lesson_screen.dart';
import '../widgets/app_drawer.dart';
import '../widgets/drop_down_list.dart';
import 'package:provider/provider.dart';
import 'package:shared_preferences/shared_preferences.dart';

class AutoExamRequestScreen extends StatefulWidget {
  const AutoExamRequestScreen({Key? key}) : super(key: key);

  @override
  _AutoExamRequestScreen createState() => _AutoExamRequestScreen();
}

class _AutoExamRequestScreen extends State<AutoExamRequestScreen> {
  final _scaffoldKey = GlobalKey<ScaffoldState>();

  late ActivateRequestProvider provider;
  List<Exam> exams = [];

  @override
  void initState() {
    super.initState();
    provider = context.read<ActivateRequestProvider>();
    provider.getMaterials(context);
    provider.periods.clear();
  }

  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size;
    final provider = context.read<ActivateRequestProvider>();
    return Selector<ActivateRequestProvider, bool>(
      selector: (context, provider) => provider.isLoading,
      builder: (context, isLoading, child) {
        return AppLoadingOverlay(
          isLoading: isLoading,
          child: Scaffold(
            backgroundColor: mainColor,
            key: _scaffoldKey,
            appBar: AppBar(
              elevation: 0,

              backgroundColor: mainColor,
              title: const Text( "Auto Exam",style: TextStyle(fontSize: 23),),
              centerTitle: true,
              leading: IconButton(
                icon: const Icon(Icons.arrow_back),
                onPressed: () {
                  Navigator.pop(context);
                },
              ),
              actions: [
                IconButton(
                  onPressed: () {
                    _scaffoldKey.currentState!.openDrawer();
                  },
                  icon: Icon(
                    Icons.menu,
                    color: Colors.white,
                  ),
                )
              ],
            ),
            drawer: const DrawerWidget(),
            body: Container(
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

                child: Column(
                  children: [
                    Padding(
                      padding: const EdgeInsets.all(10.0),
                      child: Selector<ActivateRequestProvider, MaterialModel?>(
                          selector: (_, provider) => provider.selectedMaterial,
                          builder: (context, selectedMaterial, _) {
                            final materials = provider.materials;
                            return Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                Padding(
                                  padding: const EdgeInsets.all(8.0),
                                  child: Text("please select material name",style: subTitleUpStyle,),
                                ),
                                DropdownButtonFormField<MaterialModel>(
                                  items: materials.map((e) {
                                    return DropdownMenuItem(child: Text(e.name!), value: e);
                                  }).toList(),
                                  onChanged: (value) {
                                    provider.selectMaterial(value);
                                  },
                                  value: selectedMaterial,
                                  hint: const Text("Choose material"),
                                  isExpanded: true,
                                  decoration: const InputDecoration(
                                    isDense: true,
                                    border: OutlineInputBorder(),
                                  ),
                                ),
                              ],
                            );
                          }),
                    ),
                    Expanded(
                      child: Builder(builder: (context) {
                        if (provider.error != null) {
                          return Center(
                            child: Column(
                              children: [
                                Text(provider.error!, textAlign: TextAlign.center),
                                GestureDetector(
                                  onTap: () => provider.getPeriods(),
                                  child: const Text("Retry", style: TextStyle(decoration: TextDecoration.underline)),
                                )
                              ],
                            ),
                          );
                        }

                        if (provider.periods.isEmpty && !provider.isLoading) {
                          return const Center(
                            child: Text("There is no periods for this material",textAlign: TextAlign.center),
                          );
                        }
                        return ListView.separated(
                          itemCount: provider.periods.length,
                          separatorBuilder: (_, __) => const SizedBox(height: 5),
                          itemBuilder: (context, index) {
                            final period = provider.periods[index];
                            return Selector<ActivateRequestProvider, List<PeriodModel>>(
                              shouldRebuild: (_, __) => true,
                              selector: (context, provider) => provider.selectedPeriods,
                              builder: (context, periods, child) {
                                return CheckboxListTile(
                                  title: Text(period.name),
                                  value: periods.any((p) => p.id == period.id),
                                  onChanged: (value) => provider.togglePeriodBox(period),
                                );
                              },
                            );
                          },
                        );
                      }),
                    ),
                    Selector<ActivateRequestProvider, MaterialModel?>(
                      shouldRebuild: (previous, next) {
                        return (previous == null || next == null);
                      },
                      selector: (context, provider) => provider.selectedMaterial,
                      builder: (context, value, child) {
                        if (value == null) return const SizedBox.shrink();
                        return Padding(
                          padding: const EdgeInsets.all(20.0),
                          child: ElevatedButton(
                            onPressed: () async{
                             await context.read<DataHandler>().getAutoExamByFilter([provider.selectedMaterial!.id!], 0 , [ provider.selectedPeriods[0].id!], context).then((value){
                                setState(() {
                                  exams = context.read<DataHandler>().autoExamsList;
                                  // exams.forEach((element) {_exams.add(element.examName); });
                                });
                              });

                              Navigator.push(context, MaterialPageRoute(builder: (context) =>  DoAutoExam(exams[0])));
    },
                            style: ElevatedButton.styleFrom(backgroundColor: mainColor),
                            child: const Center(child: Text("Create Exam")),
                          ),
                        );
                      },
                    )
                  ],
                ),
              ),
            ),
          ),
        );
      },
    );
  }
}
