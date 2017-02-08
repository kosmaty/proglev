const gulp = require('gulp');
var uglify = require('gulp-uglify');
var concat = require('gulp-concat');
var pkg = require('./package.json');
const babel = require('gulp-babel');

var paths = {
    js: [
        "assets/ProgLevModule.js",
        "assets/add-pregnancy-controller.js",
        "assets/index-controller.js",
        "assets/pregnacy-service.js",
        "assets/pregnancy-details-controller.js"
    ]
};

gulp.task('default', () =>
    gulp.src(paths.js)
        .pipe(concat('ProgLev.js'))
        .pipe(babel({
            presets: ['es2015']
        }))
        .pipe(gulp.dest('../resources/public/assets/js'))
);