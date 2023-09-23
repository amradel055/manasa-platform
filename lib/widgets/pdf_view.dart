import 'package:flutter/material.dart';
import 'package:full_mark/constants/strings.dart';
import 'package:internet_file/internet_file.dart';
import 'package:pdfx/pdfx.dart';

class PdfViewerPage extends StatefulWidget {
  const PdfViewerPage({Key? key, required this.url}) : super(key: key);
  final String url;

  @override
  _PdfViewerPageState createState() => _PdfViewerPageState();
}

class _PdfViewerPageState extends State<PdfViewerPage> {

  // late PdfDocument doc;
  late final pdfController = PdfController(
    document: PdfDocument.openData(InternetFile.get("$documentUrl${widget.url}")),
  );

  @override
  void initState() {
    super.initState();
    // initialize();
  }

  // initialize() async {
  //   doc = await PdfDocument.openData(InternetFile.get('https://github.com/ScerIO/packages.flutter/raw/fd0c92ac83ee355255acb306251b1adfeb2f2fd6/packages/native_pdf_renderer/example/assets/sample.pdf'));
  // }

  @override
  Widget build(BuildContext context) {
    print(widget.url);
    return Scaffold(
      appBar: AppBar(
        title: Text(
          "Flutter PDF Viewer",
          style: TextStyle(fontWeight: FontWeight.bold),
        ),
      ),
      body:PdfView(
        controller: pdfController,
      ),
    );
  }
}
