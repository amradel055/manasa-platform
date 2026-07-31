// To parse this JSON data, do
//
//     final materialModel = materialModelFromJson(jsonString);

import 'package:meta/meta.dart';
import 'dart:convert';

List<MaterialModel> materialModelFromJson(dynamic str) => List<MaterialModel>.from(str.map((x) => MaterialModel.fromJson(x)));

String materialModelToJson(List<MaterialModel> data) => json.encode(List<dynamic>.from(data.map((x) => x.toJson())));

class MaterialModel {
  MaterialModel({
     this.id,
     this.name,
  });

   int ?id;
   String ?name;

  factory MaterialModel.fromJson(Map<String, dynamic> json) => MaterialModel(
    id: json["id"],
    name: json["name"],
  );

  Map<String, dynamic> toJson() => {
    "id": id,
    "name": name,
  };
}
