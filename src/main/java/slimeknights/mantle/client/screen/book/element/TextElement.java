package slimeknights.mantle.client.screen.book.element;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import slimeknights.mantle.client.book.action.StringActionProcessor;
import slimeknights.mantle.client.book.data.element.TextData;
import slimeknights.mantle.client.screen.book.TextDataRenderer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Environment(EnvType.CLIENT)
public class TextElement extends SizedBookElement {

  public TextData[] text;
  private final List<Text> tooltip = new ArrayList<Text>();

  private boolean doAction = false;

  public TextElement(int x, int y, int width, int height, String text) {
    this(x, y, width, height, new TextData(text));
  }

  public TextElement(int x, int y, int width, int height, Collection<TextData> text) {
    this(x, y, width, height, text.toArray(new TextData[0]));
  }

  public TextElement(int x, int y, int width, int height, TextData... text) {
    super(x, y, width, height);

    this.text = text;
  }

  @Override
  public void draw(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks, TextRenderer fontRenderer) {
    String action = TextDataRenderer.drawText(matrixStack, this.x, this.y, this.width, this.height, this.text, mouseX, mouseY, fontRenderer, this.tooltip);

    if (this.doAction) {
      this.doAction = false;
      StringActionProcessor.process(action, this.parent);
    }
  }

  @Override
  public void drawOverlay(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks, TextRenderer fontRenderer) {
    if (this.tooltip.size() > 0) {
      TextDataRenderer.drawTooltip(matrixStack, this.tooltip, mouseX, mouseY, fontRenderer);
      this.tooltip.clear();
    }
  }

  @Override
  public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
    if (mouseButton == 0) {
      this.doAction = true;
    }
  }
}
