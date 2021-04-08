// Đây là những thư mục code sử dụng cho nội tại những ứng dụng gần giống nhau.
// VD như thư mục home của các ứng dụng thuộc tài khoản languageteststudio là giống hệt nhau và được đưa lên git

let libs = [
    // {path: "app/layouts/details", git: "https://quanptit@bitbucket.org/languageteststudio/layout_detail.git"},
    // {path: "app/layouts/home", git: "https://quanptit@bitbucket.org/languageteststudio/layout_home.git"},
    // {path: "app/layouts/settings", git: "https://quanptit@bitbucket.org/languageteststudio/layout_setting.git"},
    // {path: "app/utils/commons", git: "https://quanptit@bitbucket.org/languageteststudio/ultis_commons.git"},
    // { path: "android/app/src/main/java/com/app", git: "https://quanptit@bitbucket.org/rn-lib-js/java-app-ads-new-start.git" }
];




// #region libs
console.log("//////////////////////////////////");
console.log("//////////////////////////////////");
console.log("//////////////////////////////////");
console.log("//////////////////////////////////");
console.log("====== Start update local common code ====");

//#region Common function
let shell = require('shelljs');
let shellTest = shell.test;
let cd = shell.cd;
let echo = shell.echo;
let exec = shell.exec;
let rootDir = shell.pwd();

function fileExist(subPath) {
    return shellTest('-e', subPath)
}

function goToDir(subPath) {
    if (fileExist(subPath)) {
        cd(subPath);
        echo("Current Dir: " + shell.pwd());
        return true;
    }
    echo("file not exits => can't cd to: " + subPath);
    return false;
}

function gotoRootDir() {
    cd(rootDir);
    echo("Current Dir: " + shell.pwd());
}
//#endregion
for (let itemObj of libs) {
    let path = itemObj.path;
    if (fileExist(path + '/.git')) {
        goToDir(path);
        echo('Exist .git => Update libs');
        let code = exec("git pull origin master").code;
        gotoRootDir();
        if (code !== 0) {
            echo("//////// Update libs FAIL ////////");
            echo("//////// Update libs FAIL ////////");
            echo("//////// Update libs FAIL ////////");
            echo("//////// Update libs FAIL ////////");
            return;
        }
    } else {
        echo("////////  Thư mục không tồn tại, thêm thủ công trước ////////: " + path);
        echo("////////  Thư mục không tồn tại, thêm thủ công trước ////////: " + path);
        echo("////////  Thư mục không tồn tại, thêm thủ công trước ////////: " + path);
        return;
    }
}
//#endregion

