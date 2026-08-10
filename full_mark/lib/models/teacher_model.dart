import 'dart:convert';

List<TeacherModel> teacherModelFromJson(dynamic str) => List<TeacherModel>.from(str.map((x) => TeacherModel.fromJson(x)));

String teacherModelToJson(List<TeacherModel> data) => json.encode(List<dynamic>.from(data.map((x) => x.toJson())));

class TeacherModel {
  TeacherModel({
     this.description,
     this.id,
     this.image,
     this.name,
     this.imageLabel,
  });

  final String? description;
  // Teachers are now identified by a user_service UUID, not a legacy int id.
  final String? id;
  final String ?image;
  final String? name;
  final String? imageLabel;

  factory TeacherModel.fromJson(Map<String, dynamic> json) => TeacherModel(
    description: json["description"],
    id: json["id"],
    image: json["image"],
    name: json["name"] ?? "",
    imageLabel: json["imageLabel"],
  );

  Map<String, dynamic> toJson() => {
    "description": description,
    "id": id,
    "image": image,
    "name": name,
    "imageLabel": imageLabel,
  };
}
