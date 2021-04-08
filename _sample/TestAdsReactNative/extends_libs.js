
let myRN = [ // các lib myRN sẽ được add vào thư mục my-rn
    { name: "base-cpn" },
    { name: "base-utils" },
    { name: "common-style" },
    { name: "assets" },
    { name: "paper-commons" },

    {name: "ads"},
    { name: "my-ads" },

    // { name: "clickable-zoom-image" },
    // { name: "common-hooks" },
    // { name: "dialog" },
    // { name: "left-menu" },
    // { name: "select-language" },
    // { name: "sound-simple" },
    // { name: "video-libs" },
    
    // { name: "audio-play-control" },
];

let appCommon = [ // các lib myRN sẽ được add vào thư mục app-common
    // { name: "app-loading" },
    // { name: "download-lesson" },
    // { name: "lesson-detail" },
    // { name: "list-lessons" },
    // { name: "questions" },
    // { name: "user-data" }    
];


let defaultGitDir = "https://quanptit@bitbucket.org/rn-lib-js/";
console.log("====== Start Clone Or update External Git Libs ====");

//#region libs ==============================
let shell = require('shelljs');
let echo = shell.echo;
let existDir = shell.test;
let exec = shell.exec;
let cd = shell.cd;

//#region utils function
function printFail(msg) {
    echo("//////// " + msg + " ////////");
    echo("//////// " + msg + " ////////");
    echo("//////// " + msg + " ////////");
    echo("//////// " + msg + " ////////");
}
//#endregion

// thực hiện update / clone thư mục dưới một mức, rồi cd quay về thư mục cha
function updateOrCloneChildDir(arrayLibs) {
    for (let itemObj of arrayLibs) {
        let item = itemObj.name;
        echo(item);
        if (existDir('-e', item + '/.git')) {
            echo('Exist .git => Update libs');
            cd(item);
            let code = exec("git pull origin master").code;
            cd('..');
            if (code !== 0) {
                printFail("Update libs FAIL");
                return;
            }
        } else {
            let git = itemObj.git;
            if (git == null)
                git = defaultGitDir + item + ".git";
            echo('Not Exist .git => Clone libs');
            let code = exec("git clone " + git).code;
            if (code !== 0) {
                printFail("Clone libs FAIL");
                return;
            }
        }
    }
}

//#endregion


cd("my-rn");
updateOrCloneChildDir(myRN);
cd('..');

cd("app-common");
updateOrCloneChildDir(appCommon);
cd('..');

/////////// TEST ////////////////
// cd("app-common");
// for (let itemObj of appCommon) {
//     let item = itemObj.name;
//     echo("======== " + item + " ===============");
//     cd(item)
//     exec("git init ")
//     exec("git remote add origin https://quanptit@bitbucket.org/rn-lib-js/" + item + ".git");
//     exec("git add *")
//     exec("git commit -m \"release\"")
//     exec("git push -f origin master")
//     cd('..');
// }
// cd('..');

