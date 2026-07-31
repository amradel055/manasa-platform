import 'dart:convert';

List<PeriodModel> periodModelFromJson(dynamic str) => List<PeriodModel>.from(str.map((x) => PeriodModel.fromJson(x)));

String periodModelToJson(List<PeriodModel> data) => json.encode(List<dynamic>.from(data.map((x) => x.toJson())));

class PeriodModel {
  PeriodModel({
    required this.id,
    required this.materialId,
    required this.name,
     this.parent,
  });

  final int id;
  final int materialId;
  final String name;
   int? parent;

  factory PeriodModel.fromJson(Map<String, dynamic> json) => PeriodModel(
    id: json["id"],
    materialId: json["materialId"],
    name: json["name"],
    parent: json["parent"],
  );

  Map<String, dynamic> toJson() => {
    "id": id,
    "materialId": materialId,
    "name": name,
    "parent": parent,
  };
}
