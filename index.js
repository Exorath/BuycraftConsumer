/* eslint-env node */
module.exports = function(context, req) {
    context.res = {
            // status: 200, /* Defaults to 200 */
            body: {msg : "Helloworld!"}
        };
    context.done();
