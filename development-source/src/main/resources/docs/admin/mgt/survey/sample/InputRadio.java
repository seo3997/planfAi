package edu.vt.ward.survey;


import com.whomade.kycarrots.mgt.survey.vo.TbQuestion;
import jakarta.servlet.http.HttpServletRequest;

public class InputRadio extends OptionList {
  private int minOptions = 2;

  public InputRadio (String surveyId) {
    super (surveyId);
    super.setMinOptions ( this.minOptions );
    super.setInputType ( "radio" );
  }

  public InputRadio (String surveyId, TbQuestion tbQuestion ) {
    super (surveyId, tbQuestion, "inputRadio" ); // assign options and questionText
    super.setMinOptions ( this.minOptions );
    super.setInputType ( "radio" );
  }

  // outputs text e.g. "Multiple choice - pick one" or "Short answer - one line"
  public String getQuestionTypeTextHTML () {
    return "Multiple choice - pick one";
  }

  public TbQuestion getTbQuestion ( int mode ) {
	  TbQuestion inputRadio = super.getTbQuestion(mode);
	  inputRadio.setQUESTION_TYPE("inputRadio");
	  return inputRadio;
	}


  public String getEditFormHTML ( int sectionNr, int questionNr, String type, String inputErrorId ) {
    StringBuffer s = new StringBuffer ();

    s.append ( "<h2>" + this.getQuestionTypeTextHTML () + "</h2>\n" );
    s.append ( super.getEditFormHTML ( sectionNr, questionNr, type, inputErrorId ) );

    return s.toString ();
  }

  public String getEditFormHTML_New ( int sectionNr, int questionNr, String type,String above, String inputErrorId ) {
	    StringBuffer s = new StringBuffer ();

	    s.append ( "<h2>" + this.getQuestionTypeTextHTML () + "</h2>\n" );
	    s.append ( super.getEditFormHTML_New ( sectionNr, questionNr, type,above, inputErrorId ) );

	    return s.toString ();
	  }
  
  public String makeEntryChanges ( int sectionNr, int questionNr, HttpServletRequest request ) {
    try {
      String option = (String) request.getParameter ( "q_" + sectionNr + "_" + questionNr );
      if ( option != null ) {
        int optionSelected  = Integer.parseInt ( option );
        if ( optionSelected < this.getNumOptions() )
          ((Option) options.get ( optionSelected ) ).setSelected ( true );
      }

      String other = (String) request.getParameter ( "q_" + sectionNr + "_" + questionNr + "_other" );
      if ( other != null )
        this.setOtherShortAnswer ( other );
    }
    catch ( Exception e ) { // response to user input error
      return "error";
    }

    return null; // no input error occurred
  }
}
