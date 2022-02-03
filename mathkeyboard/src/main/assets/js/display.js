const HTMLCursor = `\\html'&lt;span class="blinking pointer">|&lt;/span>'`;
//var operandHolder = "\\html'&lt;span class=\"operandHolder\">?&lt;/span>'";

function showContent(content, cursorPos){
    // insert HTML cursor before displaying content
    const beforeCursor = content.slice(0, cursorPos);
    const afterCursor = content.slice(cursorPos);
    content = beforeCursor + HTMLCursor + afterCursor;

    $("#display-content").html(content);
    parseMathDisplay();
}

function parseMathDisplay() {
    const displayEl = document.getElementById('display-content');
    const innerHtml = displayEl.innerHTML;
    if (isTrustHtml(innerHtml)) {
    // disable untrusted html again
        M.MathPlayer = false;
        M.trustHtml = true;
    }

    M.parseMath(displayEl);

    // enable untrusted html again
    M.MathPlayer = true;
    M.trustHtml = false;
}

function isTrustHtml(content) {
    return content.indexOf(`\html`) == content.lastIndexOf(`\html`);
}

$(document).ready(function(){
    showContent("$$", 1); // init screen
});