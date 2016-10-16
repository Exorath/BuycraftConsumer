/* eslint-env node */
module.exports = function(context, req) {
    var content = {msg: "helloworld! Continuous deploymend!"};
    context.res = {
            // status: 200, /* Defaults to 200 */
            body: content
    };
    context.done();
}

