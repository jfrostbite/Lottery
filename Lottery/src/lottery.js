/**
 * Created by kevin on 2016/10/5.
 */
function hex_md5(e) {
    return binl2hex(core_md5(str2binl(e), e.length * chrsz))
}
function b64_md5(e) {
    return binl2b64(core_md5(str2binl(e), e.length * chrsz))
}
function str_md5(e) {
    return binl2str(core_md5(str2binl(e), e.length * chrsz))
}
function hex_hmac_md5(e, t) {
    return binl2hex(core_hmac_md5(e, t))
}
function b64_hmac_md5(e, t) {
    return binl2b64(core_hmac_md5(e, t))
}
function str_hmac_md5(e, t) {
    return binl2str(core_hmac_md5(e, t))
}
function md5_vm_test() {
    return hex_md5("abc") == "900150983cd24fb0d6963f7d28e17f72"
}
function core_md5(e, t) {
    e[t >> 5] |= 128 << t % 32, e[(t + 64 >>> 9 << 4) + 14] = t;
    var n = 1732584193, r = -271733879, i = -1732584194, s = 271733878;
    for (var o = 0; o < e.length; o += 16) {
        var u = n, a = r, f = i, l = s;
        n = md5_ff(n, r, i, s, e[o + 0], 7, -680876936), s = md5_ff(s, n, r, i, e[o + 1], 12, -389564586), i = md5_ff(i, s, n, r, e[o + 2], 17, 606105819), r = md5_ff(r, i, s, n, e[o + 3], 22, -1044525330), n = md5_ff(n, r, i, s, e[o + 4], 7, -176418897), s = md5_ff(s, n, r, i, e[o + 5], 12, 1200080426), i = md5_ff(i, s, n, r, e[o + 6], 17, -1473231341), r = md5_ff(r, i, s, n, e[o + 7], 22, -45705983), n = md5_ff(n, r, i, s, e[o + 8], 7, 1770035416), s = md5_ff(s, n, r, i, e[o + 9], 12, -1958414417), i = md5_ff(i, s, n, r, e[o + 10], 17, -42063), r = md5_ff(r, i, s, n, e[o + 11], 22, -1990404162), n = md5_ff(n, r, i, s, e[o + 12], 7, 1804603682), s = md5_ff(s, n, r, i, e[o + 13], 12, -40341101), i = md5_ff(i, s, n, r, e[o + 14], 17, -1502002290), r = md5_ff(r, i, s, n, e[o + 15], 22, 1236535329), n = md5_gg(n, r, i, s, e[o + 1], 5, -165796510), s = md5_gg(s, n, r, i, e[o + 6], 9, -1069501632), i = md5_gg(i, s, n, r, e[o + 11], 14, 643717713), r = md5_gg(r, i, s, n, e[o + 0], 20, -373897302), n = md5_gg(n, r, i, s, e[o + 5], 5, -701558691), s = md5_gg(s, n, r, i, e[o + 10], 9, 38016083), i = md5_gg(i, s, n, r, e[o + 15], 14, -660478335), r = md5_gg(r, i, s, n, e[o + 4], 20, -405537848), n = md5_gg(n, r, i, s, e[o + 9], 5, 568446438), s = md5_gg(s, n, r, i, e[o + 14], 9, -1019803690), i = md5_gg(i, s, n, r, e[o + 3], 14, -187363961), r = md5_gg(r, i, s, n, e[o + 8], 20, 1163531501), n = md5_gg(n, r, i, s, e[o + 13], 5, -1444681467), s = md5_gg(s, n, r, i, e[o + 2], 9, -51403784), i = md5_gg(i, s, n, r, e[o + 7], 14, 1735328473), r = md5_gg(r, i, s, n, e[o + 12], 20, -1926607734), n = md5_hh(n, r, i, s, e[o + 5], 4, -378558), s = md5_hh(s, n, r, i, e[o + 8], 11, -2022574463), i = md5_hh(i, s, n, r, e[o + 11], 16, 1839030562), r = md5_hh(r, i, s, n, e[o + 14], 23, -35309556), n = md5_hh(n, r, i, s, e[o + 1], 4, -1530992060), s = md5_hh(s, n, r, i, e[o + 4], 11, 1272893353), i = md5_hh(i, s, n, r, e[o + 7], 16, -155497632), r = md5_hh(r, i, s, n, e[o + 10], 23, -1094730640), n = md5_hh(n, r, i, s, e[o + 13], 4, 681279174), s = md5_hh(s, n, r, i, e[o + 0], 11, -358537222), i = md5_hh(i, s, n, r, e[o + 3], 16, -722521979), r = md5_hh(r, i, s, n, e[o + 6], 23, 76029189), n = md5_hh(n, r, i, s, e[o + 9], 4, -640364487), s = md5_hh(s, n, r, i, e[o + 12], 11, -421815835), i = md5_hh(i, s, n, r, e[o + 15], 16, 530742520), r = md5_hh(r, i, s, n, e[o + 2], 23, -995338651), n = md5_ii(n, r, i, s, e[o + 0], 6, -198630844), s = md5_ii(s, n, r, i, e[o + 7], 10, 1126891415), i = md5_ii(i, s, n, r, e[o + 14], 15, -1416354905), r = md5_ii(r, i, s, n, e[o + 5], 21, -57434055), n = md5_ii(n, r, i, s, e[o + 12], 6, 1700485571), s = md5_ii(s, n, r, i, e[o + 3], 10, -1894986606), i = md5_ii(i, s, n, r, e[o + 10], 15, -1051523), r = md5_ii(r, i, s, n, e[o + 1], 21, -2054922799), n = md5_ii(n, r, i, s, e[o + 8], 6, 1873313359), s = md5_ii(s, n, r, i, e[o + 15], 10, -30611744), i = md5_ii(i, s, n, r, e[o + 6], 15, -1560198380), r = md5_ii(r, i, s, n, e[o + 13], 21, 1309151649), n = md5_ii(n, r, i, s, e[o + 4], 6, -145523070), s = md5_ii(s, n, r, i, e[o + 11], 10, -1120210379), i = md5_ii(i, s, n, r, e[o + 2], 15, 718787259), r = md5_ii(r, i, s, n, e[o + 9], 21, -343485551), n = safe_add(n, u), r = safe_add(r, a), i = safe_add(i, f), s = safe_add(s, l)
    }
    return Array(n, r, i, s)
}
function md5_cmn(e, t, n, r, i, s) {
    return safe_add(bit_rol(safe_add(safe_add(t, e), safe_add(r, s)), i), n)
}
function md5_ff(e, t, n, r, i, s, o) {
    return md5_cmn(t & n | ~t & r, e, t, i, s, o)
}
function md5_gg(e, t, n, r, i, s, o) {
    return md5_cmn(t & r | n & ~r, e, t, i, s, o)
}
function md5_hh(e, t, n, r, i, s, o) {
    return md5_cmn(t ^ n ^ r, e, t, i, s, o)
}
function md5_ii(e, t, n, r, i, s, o) {
    return md5_cmn(n ^ (t | ~r), e, t, i, s, o)
}
function core_hmac_md5(e, t) {
    var n = str2binl(e);
    n.length > 16 && (n = core_md5(n, e.length * chrsz));
    var r = Array(16), i = Array(16);
    for (var s = 0; s < 16; s++)r[s] = n[s] ^ 909522486, i[s] = n[s] ^ 1549556828;
    var o = core_md5(r.concat(str2binl(t)), 512 + t.length * chrsz);
    return core_md5(i.concat(o), 640)
}
function safe_add(e, t) {
    var n = (e & 65535) + (t & 65535), r = (e >> 16) + (t >> 16) + (n >> 16);
    return r << 16 | n & 65535
}
function bit_rol(e, t) {
    return e << t | e >>> 32 - t
}
function str2binl(e) {
    var t = Array(), n = (1 << chrsz) - 1;
    for (var r = 0; r < e.length * chrsz; r += chrsz)t[r >> 5] |= (e.charCodeAt(r / chrsz) & n) << r % 32;
    return t
}
function binl2str(e) {
    var t = "", n = (1 << chrsz) - 1;
    for (var r = 0; r < e.length * 32; r += chrsz)t += String.fromCharCode(e[r >> 5] >>> r % 32 & n);
    return t
}
function binl2hex(e) {
    var t = hexcase ? "0123456789ABCDEF" : "0123456789abcdef", n = "";
    for (var r = 0; r < e.length * 4; r++)n += t.charAt(e[r >> 2] >> r % 4 * 8 + 4 & 15) + t.charAt(e[r >> 2] >> r % 4 * 8 & 15);
    return n
}
function binl2b64(e) {
    var t = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/", n = "";
    for (var r = 0; r < e.length * 4; r += 3) {
        var i = (e[r >> 2] >> 8 * (r % 4) & 255) << 16 | (e[r + 1 >> 2] >> 8 * ((r + 1) % 4) & 255) << 8 | e[r + 2 >> 2] >> 8 * ((r + 2) % 4) & 255;
        for (var s = 0; s < 4; s++)r * 8 + s * 6 > e.length * 32 ? n += b64pad : n += t.charAt(i >> 6 * (3 - s) & 63)
    }
    return n
}
var hexcase = 0, b64pad = "", chrsz = 8;
(function () {
    function e(e) {
        e = e || {}, this.activeId = e.activeId, this.verifyCode = e.verifyCode, this.chance = e.chance || 0, this.qid = e.qid;
        var t = e.drawer || "base";
        this.drawAPI = "http://lottery.mobilem.360.cn/turntable/" + t,
            this.drawUrl = this.drawAPI + "/draw?jscallback=?",
            this.feedbackUrl = this.drawAPI + "/useraddress?jscallback=?",
            this.addChanceUrl = this.drawAPI + "/outdealtimes?jscallback=?",
            this.getChanceUrl = this.drawAPI + "/getcurtimes?jscallback=?",
            this.allLuckInfoUrl = this.drawAPI + "/drawlist?jscallback=?",
            this.myLuckInfoUrl = this.drawAPI + "/praiselist?jscallback=?",
            this.myLuckInfonumUrl = this.drawAPI + "/praisesumlist?jscallback=?"
    }

    $.extend(e.prototype, {
        _request: function (e) {
            var t = this, n = {active: this.activeId, mid: this.getMID(), __: (new Date).getTime()};
            return $.extend(n, e.data || {}), $.ajax({
                type: "get",
                url: e.url,
                data: n,
                dataType: "jsonp",
                success: function (t) {
                    t && t.status == "ok" ? e.onSuccess && e.onSuccess(t.data) : e.onError && e.onError(t && t.data), e.onComplete && e.onComplete(t)
                },
                error: function () {
                    e.onError && e.onError()
                }
            }), this
        }, run: function (e) {
            var t = this;
            this.chance = Math.max(0, this.chance - 1);
            if (this.addChanceLocked) {
                setTimeout(function () {
                    t.run()
                }, 100);
                return
            }
            var n = e ? this.drawAPI + "/draw?pond=" + e + "&jscallback=?" : this.drawUrl;
            this._request({
                url: n, onSuccess: function (e) {
                    t.showResult(e)
                }, onError: function (e) {
                    t.showResult({drawtype: "-1"})
                }
            })
        }, getMID: function () {
            return this.qid || window.mgamer && mgamer.env.getmid() || window.qStore && qStore.util.getMID()
        }, getChance: function (e) {
            if (arguments.length == 0)return this.chance;
            var t = this;
            return this._request({
                url: this.getChanceUrl, onSuccess: function (n) {
                    t.chance = n, e(n)
                }, onError: function () {
                    t.chance = 0, e(0)
                }
            })
        }, addChance: function (e, t) {
            this.addChanceLocked = !0;
            var n = this;
            return this._request({
                url: this.addChanceUrl,
                data: {type: "6", verify: hex_md5(this.verifyCode + this.getMID())},
                onSuccess: function (t) {
                    n.chance += 1, e && e(t)
                },
                onError: t,
                onComplete: function (e) {
                    n.addChanceLocked = !1
                }
            })
        }, getAllLuckInfo: function (e, t) {
            var n = {};
            return t && (n.active = t), this._request({url: this.allLuckInfoUrl, data: n, onSuccess: e})
        }, getMyLuckInfo: function (e, t) {
            return this._request({url: this.myLuckInfoUrl, onSuccess: e, onError: t})
        }, getMyLuckInfonum: function (e, t) {
            return this._request({url: this.myLuckInfonumUrl, onSuccess: e, onError: t})
        }, setChance: function (e) {
            this.chance = e
        }, sendInfo: function (e, t, n) {
            return this._request({url: this.feedbackUrl, data: e, onSuccess: t, onError: n})
        }, sendInfo2: function (e, t, n) {
            var r = e.elements, i = {accepter: r.accepter.value, phone: r.phone.value, address: r.address.value};
            return this.sendInfo(i, t, n)
        }, showResult: function (e) {
            e.drawtype == "-1" ? this.lostPrize(e) : this.winPrize(e), this.handleResult(e)
        }, validate: function (e, t, n) {
            var r = {
                rfilled: /\S+/,
                rphone: /^1\d{10}$/i
            }, i = {
                accepter: {filled: "\u60a8\u5c1a\u672a\u586b\u5199\u59d3\u540d"},
                phone: {
                    filled: "\u60a8\u5c1a\u672a\u586b\u5199\u624b\u673a\u53f7\u7801",
                    phone: "\u60a8\u586b\u5199\u7684\u624b\u673a\u53f7\u7801\u6709\u8bef"
                },
                address: {filled: "\u60a8\u5c1a\u672a\u586b\u5199\u6536\u8d27\u5730\u5740"}
            }, s = [];
            for (var o in i) {
                var u = e.elements[o];
                if (!u)continue;
                var a = $.trim(u.value);
                for (var f in i[o]) {
                    var l = r["r" + f];
                    if (!l)continue;
                    l.lastIndex = 0, l.test(a) || s.push({el: u, tip: i[o][f]})
                }
            }
            return s
        }, winPrize: function () {
        }, lostPrize: function () {
        }, handleResult: function () {
        }
    }), window.Lottery = e
})()

//--------------------------------------------------------------------------------------------------------------------------------------------
(function () {
    function e() {
        this._init.apply(this, arguments)
    }

    String.prototype.format = function () {
        var e = arguments, t = 0;
        return this.replace(/(%s)/g, function () {
            return e[t++] || ""
        })
    }, window.util = {
        getScrollTop: function () {
            var e = 0;
            return document.documentElement && document.documentElement.scrollTop ? e = document.documentElement && document.documentElement.scrollTop : document.body && (e = document.body.scrollTop), e
        }, getScrollLeft: function () {
            var e = 0;
            return document.documentElement && document.documentElement.scrollTop ? e = document.body.scrollLeft || document.documentElement.scrollLeft : document.body && (e = document.body.scrollLeft), e
        }, _getWinSize: function () {
            var e = $(window);
            return [$(document).width(), e.height()]
        }, toArray: function (e, t) {
            return [].slice.call(e, t || 0)
        }, cmd: function (e) {
            try {
                return AndroidWebview[e].apply(AndroidWebview, this.toArray(arguments, 1))
            } catch (t) {
                return window.console && console.log([].join.call(arguments, "|")), undefined
            }
        }, easeInOut: function (e, t, n, r) {
            return (e /= r / 2) < 1 ? -n / 2 * (Math.sqrt(1 - e * e) - 1) + t : n / 2 * (Math.sqrt(1 - (e -= 2) * e) + 1) + t
        }, supportCss: function (e) {
            var t = ["webkit", "Moz", "ms", "o"], n, r = [], i = document.documentElement.style, s = function (e) {
                return e.replace(/-(\w)/g, function (e, t) {
                    return t.toUpperCase()
                })
            };
            for (n in t)r.push(s(t[n] + "-" + e));
            r.push(s(e));
            for (n in r)if (r[n] in i)return !0;
            return !1
        }, setCss: function (e, t, n) {
            var r = ["webkit", "Moz", "ms", "o"];
            for (var i in r)e.style["-" + r[i] + "-" + t] = n;
            e.style[t] = n
        }
    }, $.extend(e.prototype, {
        _init: function (e) {
            e = e || {}, this.mask = $(e.mask || ".mask"), this.popup = $(e.popup || ".popup"), this.template = $(e.template || "#popup-template").html(), this.bindEvents()
        }, setPopPostion: function () {
            var e = $(".popup"), t = e.width(), n = e.height(), r = util.getScrollTop(), i = util.getScrollLeft(), s = util._getWinSize(), o = (s[0] - t) / 2, u = (s[1] - n) / 2, a = util.getScrollTop();
            $(".js-freeMask").css({top: 0}), e.css({
                left: "50%",
                top: "50%",
                marginTop: "-" + n / 2 + "px",
                marginLeft: "-" + t / 2 + "px",
                visibility: "visible",
                "z-index": "200"
            })
        }, show: function () {
            this.mask.add(this.popup).show(), this.setPopPostion()
        }, hide: function () {
            this.mask.add(this.popup).hide()
        }, setContent: function (e) {
            var e = Mustache.render(this.template, e);
            return this.popup.html(e).add(this.mask).show(), this.setPopPostion(), this
        }, bindEvents: function () {
            var e = $(document), t = this;
            e.on("click", ".btn-close", function (e) {
                e.preventDefault(), t.hide()
            })
        }
    });
    var t = {
        prize: null, drawTimes: 0, init: function () {
            t.mid = mgamer.env.getmid(), this.popup = this.getPopup(), this.prizeBox = $(".prize"), t.getCurTimes(function (e) {
                t.drawTimes = e || 0, t.updateDrawTimes(), t.lottery = t.getLottery(), ActivityConfig && ActivityConfig.showAllprize && t.queryAllPrize(), t.bindEvents()
            }), ActivityConfig && ActivityConfig.initShareLink && ActivityConfig.initShareLink()
        }, getCurTimes: function (e) {
            $.ajax({
                type: "GET",
                url: "http://lottery.mobilem.360.cn/turntable/" + ActivityConfig.drawer + "/getcurtimes?active=" + ActivityConfig.activeId + "&qid=" + t.mid + "&jscallback=?",
                dataType: "jsonp",
                cache: !1,
                success: function (t) {
                    t.status == "ok" ? e && e(t.data) : console.log(t)
                }
            })
        }, queryAllPrize: function () {
            function e() {
                var t = $(r[0]);
                t.addClass("to-left"), setTimeout(function () {
                    n[0].appendChild(t[0]), t.removeClass("to-left"), e()
                }, 6e3)
            }

            var n = $("#luckyList"), r = n[0].getElementsByTagName("li");
            t.lottery.getAllLuckInfo(function (t) {
                if (t.length > 0) {
                    t.length = Math.max(10, t.length);
                    var r = [];
                    for (var i = 0, s = t.length; i < s; i++) {
                        var o = t[i];
                        o && r.push("<li>" + (o.phone || o.ip) + "\u7528\u6237\u83b7\u5f97" + o.drawdesc + "</li>")
                    }
                    n.append(r.join(""))
                }
                e()
            })
        }, getLottery: function () {
            var e = new Lottery({
                activeId: ActivityConfig.activeId,
                drawer: ActivityConfig.drawer,
                verifyCode: ActivityConfig.verifyCode,
                qid: t.mid
            });
            return e.winPrize = function (e) {
                e.hasPrize = !0, t.prize = e
            }, e.lostPrize = function () {
                t.prize = {drawtype: "-1"}
            }, e.handleResult = function () {
                t.drawTimes > 0 && (t.drawTimes--, t.updateDrawTimes()), t.showPrize()
            }, e
        }, startDraw: function (e) {
            t.drawTimes > 0 ? t.lottery.run(e) : (t.popup.setContent({title: "\u60a8\u5df2\u7ecf\u6ca1\u6709\u62bd\u5956\u6b21\u6570\u4e86"}), t.drawing = !1)
        }, bindCopy_zp: function () {
            $(".copycode_zp").click(function (e) {
                var t = $(".prize-code .code").html();
                try {
                    AndroidWebview.copyToClipboard(String(t)), mgamer.util.alert("\u590d\u5236\u6210\u529f")
                } catch (e) {
                    console.log(t), mgamer.util.alert("\u7248\u672c\u4e0d\u652f\u6301\uff0c\u8bf7\u624b\u52a8\u590d\u5236")
                }
            })
        }, addDrawTimes: function (e, n) {
            if (t.adding)return;
            t.adding = !0, typeof e == "function" && (n = e, e = 6), e = e || 6;
            var r = "", i = {type: e, active: ActivityConfig.activeId, qid: t.mid};
            +e === 6 && (r = "out", i.verify = hex_md5(ActivityConfig.verifyCode + t.mid)), $.ajax({
                type: "GET",
                url: "http://lottery.mobilem.360.cn/turntable/" + ActivityConfig.drawer + "/" + r + "dealtimes?jscallback=?",
                data: i,
                dataType: "jsonp",
                success: function (e) {
                    e.status === "ok" && e.data > 0 && (t.drawTimes = e.data, t.updateDrawTimes()), t.adding = !1, n && n()
                }
            }), setTimeout(function () {
                t.adding = !1
            }, 500)
        }, updateDrawTimes: function () {
            ActivityConfig && ActivityConfig.updateDrawTimes && ActivityConfig.updateDrawTimes(t.drawTimes)
        }, showPrize: function () {
            var e = t.prize;
            t.turnbox(e.drawtype, function () {
                t.drawing = !1;
                if (e.drawtype == "-1")return t.showNoPrize();
                if (e.info1)return e.isCodePrize = !0, e.drawmark = e.drawmark.replace(/#info1#/g, e.info1).replace(/#info2#/g, e.info2 || "").replace(/#info3#/g, e.info3 || ""), t.showCodePrize();
                e.isPostPrize = !0, t.showPostPrize()
            }), t.bindCopy_zp()
        }, showNoPrize: function () {
            ActivityConfig && ActivityConfig.showNoPrize && t.popup.setContent({title: ActivityConfig.noDrawMsg || "\u5f88\u9057\u61be\uff0c\u60a8\u672a\u4e2d\u5956"})
        }, showPostPrize: function () {
            var e = t.prize;
            t.popup.setContent(e)
        }, showCodePrize: function () {
            var e = t.prize, n = e.drawmark, r = n.indexOf("\u5151\u6362\u65b9\u5f0f");
            e.drawmark = n.substring(r), t.popup.setContent(e)
        }, turnbox: function (e, n) {
            var r = ActivityConfig.type;
            if (r == 1 || r == 2) {
                var i = ActivityConfig.levelIndex[e];
                isNaN(i) && (i = i[Math.floor(Math.random() * i.length)]), t.animate1(i, n)
            } else r == 3 && t.animate2(e, n)
        }, animate2: function (e, n) {
            var r = t.prizeBox, i = r.length, s, o = [], u = $(".btn-switch"), a = window.util.supportCss("transform"), f = function (e, t) {
                a ? window.util.setCss(e[0], "transform", "translateY(" + t + "px)") : e.css("marginTop", t + "px")
            };
            u.addClass("on");
            var l = function (e) {
                var t = r.eq(e), s = o[e], a = 300, c;
                s.time < a ? (s.time++, c = window.util.easeInOut(s.time, s.beginPos, s.stopPos - s.beginPos, a), f(t, c), setTimeout(function () {
                    l(e)
                }, 10)) : (f(t, s.stopIndex * r.children().eq(0).height() * -1), t.html(s.orgHtm).data("showindex", s.stopIndex), e == i - 1 && n instanceof Function && (n(), u.removeClass("on")))
            };
            r.each(function (t) {
                var n = $(this).children().length;
                if (e == -1)for (var r = 0; r < 10; r++) {
                    var i = Math.floor(Math.random() * n);
                    if (s != i) {
                        s = i;
                        break
                    }
                } else s = e - 1;
                var u = $(this).children().eq(0).height(), a = $(this).html(), f = [];
                for (var r = 0; r < 9; r++)f.push(a);
                $(this).append(f.join("")), o[t] = {
                    beginPos: $(this).data("showindex") * u * -1,
                    time: 0,
                    stopIndex: s,
                    stopPos: -1 * ($(this).height() - (n - s) * u),
                    orgHtm: a
                }, window.setTimeout(function () {
                    l(t)
                }, t * 700)
            })
        }, animate1: function (e, n) {
            function r(e) {
                window.util.setCss(c, "transform", p + "(" + e + ")")
            }

            function i() {
                f > 8 && (a = a > 45 ? a * .9 : 45), u < 14 && (a = 150), u-- > 0 ? setTimeout(function () {
                    var e = f++ % s;
                    h ? r(l[e]) : t.prizeBox.eq(e).addClass("current").siblings().removeClass("current"), i()
                }, a) : (h ? r(l[e - 1]) : t.prizeBox.eq(e - 1).addClass("current").siblings().removeClass("current"), setTimeout(n, 600))
            }

            var s = t.prizeBox.length, o = 8, u = o * s + e, a = 150, f = 0, l = ActivityConfig.pos, c = $(".current")[0], h = window.util.supportCss("transform"), p = ActivityConfig.type == 2 ? "rotate" : "translate";
            c.style.display = h ? "block" : $(c).hasClass("prize") ? "block" : "none", setTimeout(i, 0)
        }, getPopup: function () {
            return new e
        }, setPrizeBoxPos: function () {
            if (ActivityConfig.type != 3)return;
            if (t.drawing)return;
            var e = t.prizeBox;
            e.each(function (e) {
                var t = $(this), n = t.children(), r = -1 * n.eq(0).height() * t.data("showindex");
                window.util.supportCss("transform") ? window.util.setCss(this, "transform", "translateY(" + r + "px)") : t.css("marginTop", r + "px")
            })
        }, bindEvents: function () {
            var e = $(document);
            $(function () {
                if (ActivityConfig.type == 3) {
                    var e;
                    t.prizeBox.each(function (n) {
                        for (var r = 0; r < 10; r++) {
                            var i = Math.floor(Math.random() * t.prizeBox.eq(r).children().length);
                            if (e != i) {
                                e = i;
                                break
                            }
                        }
                        $(this).data("showindex", e)
                    }), window.setTimeout(function () {
                        t.setPrizeBoxPos()
                    }, 300)
                }
            }), "onorientationchange" in window ? window.addEventListener("orientationchange", t.setPrizeBoxPos, !1) : $(window).on("resize", t.setPrizeBoxPos), $(".btn-draw").on("click", function (e) {
                e.preventDefault();
                if (t.drawing)return;
                $(".app-item").hasClass("animate-button") && $(".app-item").removeClass("animate-button"), t.drawing = !0, setTimeout(function () {
                    t.startDraw(ActivityConfig.poolId || 0)
                }, 500)
            }), $(".btn-myprize").on("click", function (e) {
                e.preventDefault(), t.lottery.getMyLuckInfo(function (e) {
                    var n = {
                        title: e.length ? "\u6211\u7684\u4e2d\u5956\u5217\u8868" : "\u6211\u8fd8\u6ca1\u6709\u4e2d\u5956",
                        prizes: e
                    };
                    t.popup.setContent(n)
                })
            }), e.on("click", ".btn-editaddr", function (e) {
                e.preventDefault(), t.popup.setContent({title: "\u4fee\u6539\u5730\u5740\u4fe1\u606f", isPostPrize: !0})
            }), e.on("click", ".btn-sendInfo", function (e) {
                e.preventDefault();
                var n = $(".popup-form")[0], r = n.elements, i = t.lottery, s = i.validate(n), o = $(".form-tip");
                if (s.length) {
                    o.text(s[0].tip).show();
                    return
                }
                o.hide(), i.sendInfo({
                    accepter: r.accepter.value,
                    address: r.address.value,
                    phone: r.phone.value,
                    sendtime: 1
                }, function () {
                    t.popup.setContent({title: "\u9886\u5956\u4fe1\u606f\u5df2\u7ecf\u586b\u5199\u5b8c\u6210"})
                }, function () {
                    o.text("\u4fdd\u5b58\u7528\u6237\u5730\u5740\u9519\u8bef").show()
                })
            }), ActivityConfig.bindEvents && ActivityConfig.bindEvents(t)
        }
    };
    mgamer.ready(function (e) {
        t.init()
    })
})()