/*
 * Oliver Kniffel, 2021.
 */
package rpg.ag;

public class AdventurePrinter {

  public void print(Generated generated) {
    if (generated instanceof GeneratedText) {
      System.out.println(((GeneratedText) generated).getText());
    } else {
      print(0, (GeneratedObject) generated);
    }
  }

  public void print(int indent, GeneratedObject generated) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < indent; i++) {
      sb.append("  ");
    }
    String padding = sb.toString();

    for (GeneratedObjectProperty property : generated.getProperties()) {
      String name = property.getName();

      if (property.getValue() instanceof GeneratedText) {
        String value = ((GeneratedText) property.getValue()).getText();
        System.out.println(padding + name + ": " + value);

      } else {
        System.out.println(padding + name + ":");
        print(indent + 1, (GeneratedObject) property.getValue());

      }
    }
  }
}
