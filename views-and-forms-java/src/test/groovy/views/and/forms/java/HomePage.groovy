package views.and.forms.java

import geb.Page

class HomePage extends Page {

    static at = { title == 'Survey' }

    static url = '/'

    static content = {

    }

//    static content = {
//        inputField {
//          $('input', text: '', 0)
//        }
//        submitButton {
//          $('input', type: 'submit')
//        }
//    }

//    static content = {
//        excelLink { $('a', text: 'Excel', 0) }
//    }

}