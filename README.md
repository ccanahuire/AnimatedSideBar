# Custom Navigation Drawer in Jetpack Compose

This project demonstrates how to create a custom navigation drawer with a special animation of the content. The animation slides the content in from the side and adds an slightly rotation over the Y axis.

## Features
* The implementation is in the `FancyNavigationDrawer.kt` file which is based on the material implementation of the `NavigationDrawer`.
* This project uses `AnchoredDraggable` to manage the drawer state.

## Preview
![Side Bar Preview](preview.gif)

#### Note
This project is still under development and there are still some issues that needs to be fixed.
- Create a custom saveable to store the `AnchoredDraggableState` so it can survive configuration changes.
- We need to handle the touch events to avoid closing the Navigation drawer when touching inside the side bar.