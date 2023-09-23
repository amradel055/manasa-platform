import 'package:flutter/material.dart';
import 'package:full_mark/constants/strings.dart';
import 'package:provider/provider.dart';
import 'package:full_mark/providers/screen_providers/common_questions_provider.dart';
import 'package:full_mark/widgets/app_loading_overlay.dart';
import 'package:full_mark/widgets/expanded_card.dart';
import 'package:full_mark/widgets/image_widget.dart';

import '../constants/colors.dart';
import '../constants/text_styles.dart';
import '../models/material_model.dart';
import '../widgets/app_drawer.dart';

class CommonQuestionsScreen extends StatefulWidget {
  const CommonQuestionsScreen({Key? key}) : super(key: key);

  @override
  State<CommonQuestionsScreen> createState() => _CommonQuestionsScreenState();
}

class _CommonQuestionsScreenState extends State<CommonQuestionsScreen> {

  final _scaffoldKey = GlobalKey<ScaffoldState>();

  late CommonQuestionsProvider provider;

  @override
  void initState() {
    super.initState();
    provider = context.read<CommonQuestionsProvider>();
    provider.getMaterials(context);
  }

  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size ;

    return Selector<CommonQuestionsProvider, bool>(
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
              title: const Text( "Common Questions",style: TextStyle(fontSize: 23),),
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
                      child: Selector<CommonQuestionsProvider, MaterialModel?>(
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
                                    provider.selectMaterial(context, value);
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
                      child: Builder(
                        builder: (context) {
                          if(provider.error != null){
                            return Center(
                              child: Column(
                                children: [
                                  Text(provider.error!,textAlign: TextAlign.center),
                                  GestureDetector(
                                      onTap: () => provider.getQuestions(context),
                                      child: Text("Retry",style: TextStyle(decoration: TextDecoration.underline)))
                                ],
                              ),
                            );
                          }
                          if (provider.questions.isEmpty && !provider.isLoading) {
                            return const Center(
                              child: Text("There is no questions for this material",textAlign: TextAlign.center),
                            );
                          }
                          return ListView.separated(
                            itemCount: provider.questions.length,
                            padding: const EdgeInsets.all(15.0),
                            separatorBuilder: (_, __) => const SizedBox(height: 10),
                            itemBuilder: (context, index) {
                              final question = provider.questions[index];
                              return ExpandedCard(
                                title: Text(question!.text??""),
                                imagePath:uploadUrl+(question!.image??""),
                                children: [
                                  Padding(
                                    padding: const EdgeInsets.all(10),
                                    child: Align(alignment: AlignmentDirectional.centerStart,child: Text(question.questionAnswerDto!.text!)),
                                  ),
                                  // for(final x in question.questionAnswerDto)
                                    Column(
                                      crossAxisAlignment: CrossAxisAlignment.start,
                                      children: [
                                        ImageWidget(path:  uploadUrl+(question.questionAnswerDto!.image??""), width: double.infinity, height: 200, fit: BoxFit.cover,),
                                        Padding(
                                          padding: const EdgeInsetsDirectional.fromSTEB(10, 5, 10, 10),
                                          child: Text(question.questionAnswerDto!.text??""),
                                        ),
                                      ],
                                    ),
                                ],
                              );
                            },
                          );
                        }
                      ),
                    ),
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
