/*
Copyright (c) 2010, Yahoo! Inc. All rights reserved.
Code licensed under the BSD License:
http://developer.yahoo.com/yui/license.html
version: 3.1.0
build: 2026
*/
YUI.add('node-style', function(Y) {

(function(Y) {
/**
 * Extended Node interface for managing node styles.
 * @module node
 * @submodule node-style
 */

var methods = [
    /**
     * Returns the style's current value.
     * @method getStyle
     * @for Node
     * @param {String} attr The style attribute to retrieve. 
     * @return {String} The current value of the style property for the element.
     */
    'getStyle',

    /**
     * Returns the computed value for the given style property.
     * @method getComputedStyle
     * @param {String} attr The style attribute to retrieve. 
     * @return {String} The computed value of the style property for the element.
     */
    'getComputedStyle',

    /**
     * Sets a style property of the node.
     * @method setStyle
     * @param {String} attr The style attribute to set. 
     * @param {String|Number} val The value. 
     * @chainable
     */
    'setStyle',

    /**
     * Sets multiple style properties on the node.
     * @method setStyles
     * @param {Object} hash An object literal of property:value pairs. 
     * @chainable
     */
    'setStyles'
];
Y.Node.importMethod(Y.DOM, methods);
/**
 * Returns an array of values for each node.
 * @method getStyle
 * @for NodeList
 * @see Node.getStyle
 * @param {String} attr The style attribute to retrieve. 
 * @return {Array} The current values of the style property for the element.
 */

/**
 * Returns an array of the computed value for each node.
 * @method getComputedStyle
 * @see Node.getComputedStyle
 * @param {String} attr The style attribute to retrieve. 
 * @return {Array} The computed values for each node.
 */

/**
 * Sets a style property on each node.
 * @method setStyle
 * @see Node.setStyle
 * @param {String} attr The style attribute to set. 
 * @param {String|Number} val The value. 
 * @chainable
 */

/**
 * Sets multiple style properties on each node.
 * @method setStyles
 * @see Node.setStyles
 * @param {Object} hash An object literal of property:value pairs. 
 * @chainable
 */
Y.NodeList.importMethod(Y.Node.prototype, methods);
})(Y);
Y.mix(Y.Node.ATTRS, {
    offsetHeight: {
        setter: function(h) {
            Y.DOM.setHeight(this._node, h);
            return h;
        },

        getter: function() {
            return this._node.offsetHeight;
        }
    },

    offsetWidth: {
        setter: function(w) {
            Y.DOM.setWidth(this._node, w);
            return w;
        },

        getter: function() {
            return this._node.offsetWidth;
        }
    }
});

Y.mix(Y.Node.prototype, {
    sizeTo: function(w, h) {
        var node;
        if (arguments.length < 2) {
            node = Y.one(w);
            w = node.get('offsetWidth');
            h = node.get('offsetHeight');
        }

        this.setAttrs({
            offsetWidth: w,
            offsetHeight: h
        });
    }
});


}, '3.1.0' ,{requires:['dom-style', 'node-base']});
