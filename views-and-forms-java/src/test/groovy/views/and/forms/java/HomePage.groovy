package views.and.forms.java

import geb.Page

class HomePage extends Page {

    static at = { title.contains 'Survey' }

    static url = '/'

    static content = {
        submitButton (to:ThankyouPage){
          $("input", type: "submit")
        }

    }

}