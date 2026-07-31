import 'package:flutter/material.dart';

class AppLoadingOverlay extends StatelessWidget {
  const AppLoadingOverlay({Key? key, required this.isLoading, required this.child}) : super(key: key);
  final bool isLoading;
  final Widget child;

  @override
  Widget build(BuildContext context) {
    return AbsorbPointer(
      absorbing: isLoading,
      child: Stack(
        children: [
          child,
          if(isLoading)
            const Positioned.fill(
              child: ColoredBox(
                color: Colors.black26,
                child: Center(
                  child: CircularProgressIndicator(),
                ),
              ),
            )
        ],
      ),
    );
  }
}
