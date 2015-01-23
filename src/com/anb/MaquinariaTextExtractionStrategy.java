package com.anb;

import com.itextpdf.text.pdf.parser.*;


/**
 * Created with IntelliJ IDEA.
 * User: davidolori
 * Date: 1/14/15
 * Time: 10:16 AM
 * To change this template use File | Settings | File Templates.
 */
public class MaquinariaTextExtractionStrategy implements TextExtractionStrategy {


    private Vector lastStart;
    private Vector lastEnd;

    /** used to store the resulting String. */
    private final StringBuffer result = new StringBuffer();;

    /**
     * Creates a new text extraction renderer.
     */
    public MaquinariaTextExtractionStrategy() {
    }

    /**
     * @since 5.0.1
     */
    public void beginTextBlock() {
    }

    /**
     * @since 5.0.1
     */
    public void endTextBlock() {
    }

    /**
     * Returns the result so far.
     * @return	a String with the resulting text.
     */
    public String getResultantText(){
        return result.toString();
    }

    /**
     * Used to actually append text to the text results.  Subclasses can use this to insert
     * text that wouldn't normally be included in text parsing (e.g. result of OCR performed against
     * image content)
     * @param text the text to append to the text results accumulated so far
     */
    protected final void appendTextChunk(CharSequence text){
        result.append(text);
    }

    /**
     * Captures text using a simplified algorithm for inserting hard returns and spaces
     * @param	renderInfo	render info
     */
    public void renderText(TextRenderInfo renderInfo) {
        boolean firstRender = result.length() == 0;
        boolean hardReturn = false;

        LineSegment segment = renderInfo.getBaseline();
        Vector start = segment.getStartPoint();
        Vector end = segment.getEndPoint();

        if (!firstRender){
            Vector x0 = start;
            Vector x1 = lastStart;
            Vector x2 = lastEnd;

            // see http://mathworld.wolfram.com/Point-LineDistance2-Dimensional.html
            float dist = (x2.subtract(x1)).cross((x1.subtract(x0))).lengthSquared() / x2.subtract(x1).lengthSquared();

            float sameLineThreshold = 1f; // we should probably base this on the current font metrics, but 1 pt seems to be sufficient for the time being
            if (dist > sameLineThreshold)
                hardReturn = true;

            // Note:  Technically, we should check both the start and end positions, in case the angle of the text changed without any displacement
            // but this sort of thing probably doesn't happen much in reality, so we'll leave it alone for now
        }

        if (hardReturn){
            //System.out.println("<< Hard Return >>");
            appendTextChunk("\n");
        } else if (!firstRender){
            if (result.charAt(result.length()-1) != ' ' && renderInfo.getText().length() > 0 && renderInfo.getText().charAt(0) != ' '){ // we only insert a blank space if the trailing character of the previous string wasn't a space, and the leading character of the current string isn't a space
                float spacing = lastEnd.subtract(start).length();
                if (spacing > renderInfo.getSingleSpaceWidth()/2f){
                    appendTextChunk(" ");
                    //System.out.println("Inserting implied space before '" + renderInfo.getText() + "'");
                }
            }
        } else {
            //System.out.println("Displaying first string of content '" + text + "' :: x1 = " + x1);
        }

       // System.out.println("Encodign = "+renderInfo.getFillColor());
        //System.out.println("[" + renderInfo.getStartPoint() + "]->[" + renderInfo.getEndPoint() + "] " + renderInfo.getText());
        appendTextChunk(renderInfo.getText());

        lastStart = start;
        lastEnd = end;

    }

    /**
     * no-op method - this renderer isn't interested in image events
     * @see com.itextpdf.text.pdf.parser.RenderListener#renderImage(com.itextpdf.text.pdf.parser.ImageRenderInfo)
     * @since 5.0.1
     */
    public void renderImage(ImageRenderInfo renderInfo) {
        // do nothing - we aren't tracking images in this renderer
    }



}
