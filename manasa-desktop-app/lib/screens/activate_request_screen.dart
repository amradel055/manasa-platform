import 'dart:convert';

import 'package:desktop_screen/models/material_model.dart';
import 'package:desktop_screen/models/period_model.dart';
import 'package:desktop_screen/providers/screen_providers/activate_request_provider.dart';
import 'package:desktop_screen/widgets/app_loading_overlay.dart';
import 'package:flutter/material.dart';

import '../constants/colors.dart';
import '../constants/strings.dart';
import '../constants/text_styles.dart';
import '../models/lesson_model.dart';
import '../providers/data_handler_provider.dart';
import '../widgets/app_drawer.dart';
import '../widgets/drop_down_list.dart';
import 'package:provider/provider.dart';
import 'package:shared_preferences/shared_preferences.dart';

class ActivateRequestScreen extends StatefulWidget {
  const ActivateRequestScreen({Key? key}) : super(key: key);

  @override
  _ActivateRequestScreen createState() => _ActivateRequestScreen();
}

class _ActivateRequestScreen extends State<ActivateRequestScreen> {
  final _scaffoldKey = GlobalKey<ScaffoldState>();

  late ActivateRequestProvider provider;

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
              title: const Text( "Activate request",style: TextStyle(fontSize: 23),),
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
            body: Center(
              child: Container(
                decoration:  BoxDecoration(color:mainColor),
                width: size.width*.4,
                height: size.height*.9,
                child: Container(
                  decoration: const BoxDecoration(
                      color: Colors.white,
                      borderRadius: BorderRadius.all(
                        Radius.circular(40),

                      )),

                  child: Column(
                    children: [
                      Padding(
                        padding: const EdgeInsets.all(10.0),
                        child: Selector<ActivateRequestProvider, MaterialModel?>(
                            selector: (_, provider) => provider.selectedMaterial,
                            builder: (context, selectedMaterial, _) {
                              final materials = provider.materials;
                              return Container(
                                width: size.width*.4,
                                child: Column(
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
                                ),
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
                              onPressed: () => provider.sendRequest(context),
                              style: ElevatedButton.styleFrom(backgroundColor: mainColor),
                              child: const Center(child: Text("Send Request")),
                            ),
                          );
                        },
                      )
                    ],
                  ),
                ),
              ),
            ),
          ),
        );
      },
    );
  }
}
