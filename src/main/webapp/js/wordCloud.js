am4internal_webpackJsonp(["4859"], {
    "6JTK": function(e, t, i) {
        "use strict";
        Object.defineProperty(t, "__esModule", {
            value: !0
        });
        var r = {};
        i.d(r, "WordCloudDataItem", function() {
            return _
        }), i.d(r, "WordCloud", function() {
            return C
        }), i.d(r, "WordCloudSeriesDataItem", function() {
            return v
        }), i.d(r, "WordCloudSeries", function() {
            return x
        });
        var a = i("m4/l"),
            n = i("2I/e"),
            s = i("aM7D"),
            o = i("Vs7R"),
            l = i("C6dT"),
            u = i("p9TX"),
            h = i("vMqJ"),
            p = i("8ZqG"),
            c = i("aCit"),
            d = i("hGwe"),
            m = i("v9UT"),
            f = i("Gg2j"),
            g = i("58Sn"),
            y = i("tjMS"),
            b = i("hD5A"),
            v = function(e) {
                function t() {
                    var t = e.call(this) || this;
                    return t.className = "WordCloudSeriesDataItem", t.applyTheme(), t
                }
                return Object(a.c)(t, e), t.prototype.hide = function(t, i, r, a) {
                    return a || (a = ["value"]), e.prototype.hide.call(this, t, i, 0, a)
                }, t.prototype.setVisibility = function(t, i) {
                    i || (t ? this.setWorkingValue("value", this.values.value.value, 0, 0) : this.setWorkingValue("value", 0, 0, 0)), e.prototype.setVisibility.call(this, t, i)
                }, t.prototype.show = function(t, i, r) {
                    return r || (r = ["value"]), e.prototype.show.call(this, t, i, r)
                }, Object.defineProperty(t.prototype, "word", {
                    get: function() {
                        return this.properties.word
                    },
                    set: function(e) {
                        this.setProperty("word", e)
                    },
                    enumerable: !0,
                    configurable: !0
                }), Object.defineProperty(t.prototype, "label", {
                    get: function() {
                        var e = this;
                        if (!this._label) {
                            var t = this.component.labels.create();
                            this._label = t, this._disposers.push(t), t.parent = this.component.labelsContainer, t.isMeasured = !1, t.x = Object(y.c)(50), t.y = Object(y.c)(50), t.fontSize = 0, this.component.colors && (t.fill = this.component.colors.next()), this._disposers.push(new b.b(function() {
                                e.component && e.component.labels.removeValue(t)
                            })), this.addSprite(t), t.visible = this.visible
                        }
                        return this._label
                    },
                    enumerable: !0,
                    configurable: !0
                }), t
            }(s.b),
            x = function(e) {
                function t() {
                    var t = e.call(this) || this;
                    t._adjustedFont = 1, t.className = "WordCloudSeries", t.accuracy = 5, t.isMeasured = !0, t.minFontSize = Object(y.c)(2), t.maxFontSize = Object(y.c)(20), t.excludeWords = [], t.layout = "absolute", t.angles = [0, 0, 90], t.rotationThreshold = .7, t.minWordLength = 1, t.width = Object(y.c)(100), t.height = Object(y.c)(100), t.step = 15, t.randomness = .2, t.labels.template.horizontalCenter = "middle", t.labels.template.verticalCenter = "middle", t.itemReaderText = "{word}: {value}", t.applyTheme();
                    var i = document.createElement("canvas");
                    i.style.position = "absolute", i.style.top = "0px", i.style.left = "0px", i.style.opacity = "0.5", t._canvas = i, t._ctx = i.getContext("2d"), t._maskSprite = t.createChild(o.a);
                    var r = t.createChild(l.a);
                    return r.shouldClone = !1, r.isMeasured = !1, r.layout = "none", t.labelsContainer = r, t._spiral = r.createChild(o.a), t._spiral.fillOpacity = .1, t._spiral.strokeOpacity = 1, t._spiral.stroke = Object(p.c)("#000"), t
                }
                return Object(a.c)(t, e), t.prototype.validateDataRange = function() {
                    e.prototype.validateDataRange.call(this), this.dataItems.each(function(e) {
                        m.used(e.label)
                    })
                }, t.prototype.validate = function() {
                    e.prototype.validate.call(this), this._currentIndex = 0, this.dataItems.values.sort(function(e, t) {
                        return e.value == t.value ? 0 : e.value > t.value ? -1 : 1
                    }), this._processTimeout && this._processTimeout.dispose();
                    var t = this.innerWidth,
                        i = this.innerHeight;
                    if (t > 0 && i > 0) {
                        var r = this._ctx;
                        this._canvas.width = t, this._canvas.height = i, r.fillStyle = "white", r.fillRect(0, 0, t, i), this._points = d.spiralPoints(0, 0, t, i, 0, this.step, this.step);
                        for (var a = this.labelsContainer.rotation, n = this._points.length - 1; n >= 0; n--) {
                            var s = this._points[n];
                            if (s.x < -t / 2 || s.x > t / 2 || s.y < -i / 2 || s.y > i / 2) this._points.splice(n, 1);
                            else if (0 != a) {
                                var o = m.spritePointToSprite({
                                    x: s.x + t / 2,
                                    y: s.y + i / 2
                                }, this, this.labelsContainer);
                                s.x = o.x, s.y = o.y
                            }
                        }
                        var l = this._maskSprite;
                        if (l.path) {
                            var u = this.maxWidth,
                                h = this.maxHeight;
                            l.isMeasured = !0, l.validate();
                            var p = l.measuredWidth / l.scale,
                                c = l.measuredHeight / l.scale,
                                g = f.min(h / c, u / p);
                            g == 1 / 0 && (g = 1), l.horizontalCenter = "none", l.verticalCenter = "none", l.x = 0, l.y = 0, l.scale = 1, g = f.max(.001, g), l.horizontalCenter = "middle", l.verticalCenter = "middle", l.x = t / 2, l.y = i / 2, l.validatePosition(), this.mask = l, l.scale = g
                        }
                        this.events.isEnabled("arrangestarted") && this.dispatchImmediately("arrangestarted"), this.processItem(this.dataItems.getIndex(this._currentIndex))
                    }
                }, t.prototype.processItem = function(e) {
                    var t = this;
                    if (e) {
                        var i = this._ctx,
                            r = this.innerWidth,
                            a = this.innerHeight;
                        if (g.v(this.htmlContainer)) return this._processTimeout = this.setTimeout(function() {
                            t._currentIndex++, t.processItem(t.dataItems.getIndex(t._currentIndex))
                        }, 500), void this._disposers.push(this._processTimeout);
                        this.labelsContainer.x = r / 2, this.labelsContainer.y = a / 2;
                        var n = e.label,
                            s = g.l(n.element.node),
                            o = f.min(this.innerHeight, this.innerWidth),
                            l = m.relativeToValue(this.minFontSize, o),
                            u = m.relativeToValue(this.maxFontSize, o),
                            h = this.dataItem.values.value.low,
                            p = this.dataItem.values.value.high,
                            c = (e.value - h) / (p - h);
                        if (h == p) c = this.dataItems.length > 1 ? 1 / this.dataItems.length * 1.5 : 1;
                        var d = l + (u - l) * c * this._adjustedFont,
                            y = n.fontSize;
                        n.fontSize = d;
                        var b = 0;
                        if ((d - l) / (u - l) < this.rotationThreshold && (b = this.angles[Math.round(Math.random() * (this.angles.length - 1))]), n.fontSize = d, n.rotation = b, n.show(0), n.hardInvalidate(), n.validate(), n.measuredWidth > .95 * r || n.measuredHeight > .95 * a) return this._adjustedFont -= .1, this.invalidateDataItems(), void this.invalidate();
                        var v = n.maxLeft,
                            x = n.maxRight,
                            _ = n.maxTop,
                            C = n.maxBottom,
                            P = !0,
                            I = Math.round(Math.random() * this._points.length * this.randomness),
                            w = n.pixelX,
                            V = n.pixelY,
                            W = 0,
                            j = 0;
                        if (m.used(this.labelsContainer.rotation), this._currentIndex > 0)
                            for (; P;) {
                                if (I > this._points.length - 1) return P = !1, this._adjustedFont -= .1, void this.invalidateDataItems();
                                P = !1, W = this._points[I].x, j = this._points[I].y;
                                for (var O = n.pixelMarginLeft, S = n.pixelMarginRight, T = n.pixelMarginTop, D = {
                                        x: W + v - O,
                                        y: j + _ - T,
                                        width: x - v + O + S,
                                        height: C - _ + T + n.pixelMarginBottom
                                    }, z = this._ctx.getImageData(D.x + r / 2, D.y + a / 2, D.width, D.height).data, M = 0; M < z.length; M += Math.pow(2, this.accuracy))
                                    if (255 != z[M]) {
                                        P = !0, n.currentText.length > 3 && (0 == b && x - v < 60 && this._points.splice(I, 1), 90 == Math.abs(b) && C - _ < 50 && this._points.splice(I, 1));
                                        break
                                    } I += 5
                            }
                        0 == y ? (n.animate([{
                            property: "fontSize",
                            to: d,
                            from: y
                        }], this.interpolationDuration, this.interpolationEasing), n.x = W, n.y = j) : n.animate([{
                            property: "fontSize",
                            to: d,
                            from: y
                        }, {
                            property: "x",
                            to: W,
                            from: w
                        }, {
                            property: "y",
                            to: j,
                            from: V
                        }], this.interpolationDuration, this.interpolationEasing);
                        var F = W + r / 2,
                            L = j + a / 2;
                        i.translate(F, L);
                        var k = n.rotation * Math.PI / 180;
                        i.rotate(k), i.textAlign = "center", i.textBaseline = "middle", i.fillStyle = "blue";
                        var R = (n.fontWeight || this.fontWeight || this.chart.fontWeight || "normal") + " " + d + "px " + s;
                        if (i.font = R, i.fillText(n.currentText, 0, 0), i.rotate(-k), i.translate(-F, -L), n.showOnInit && (n.hide(0), n.show()), this.events.isEnabled("arrangeprogress")) {
                            var E = {
                                type: "arrangeprogress",
                                target: this,
                                progress: (this._currentIndex + 1) / this.dataItems.length
                            };
                            this.events.dispatchImmediately("arrangeprogress", E)
                        }
                        this._currentIndex < this.dataItems.length - 1 ? (this._processTimeout = this.setTimeout(function() {
                            t._currentIndex++, t.processItem(t.dataItems.getIndex(t._currentIndex))
                        }, 1), this._disposers.push(this._processTimeout)) : this.events.isEnabled("arrangeended") && this.dispatchImmediately("arrangeended")
                    }
                }, t.prototype.createLabel = function() {
                    return new u.a
                }, Object.defineProperty(t.prototype, "labels", {
                    get: function() {
                        if (!this._labels) {
                            var e = this.createLabel();
                            e.applyOnClones = !0, this._disposers.push(e), e.text = "{word}", e.margin(2, 3, 2, 3), e.padding(0, 0, 0, 0), this._labels = new h.e(e), this._disposers.push(new h.c(this._labels))
                        }
                        return this._labels
                    },
                    enumerable: !0,
                    configurable: !0
                }), t.prototype.createDataItem = function() {
                    return new v
                }, Object.defineProperty(t.prototype, "colors", {
                    get: function() {
                        return this.getPropertyValue("colors")
                    },
                    set: function(e) {
                        this.setPropertyValue("colors", e, !0)
                    },
                    enumerable: !0,
                    configurable: !0
                }), t.prototype.updateData = function() {
                    this.data = this.getWords(this.text)
                }, Object.defineProperty(t.prototype, "text", {
                    get: function() {
                        return this.getPropertyValue("text")
                    },
                    set: function(e) {
                        this.setPropertyValue("text", e) && this.updateData()
                    },
                    enumerable: !0,
                    configurable: !0
                }), Object.defineProperty(t.prototype, "maxCount", {
                    get: function() {
                        return this.getPropertyValue("maxCount")
                    },
                    set: function(e) {
                        this.setPropertyValue("maxCount", e) && this.updateData()
                    },
                    enumerable: !0,
                    configurable: !0
                }), Object.defineProperty(t.prototype, "minValue", {
                    get: function() {
                        return this.getPropertyValue("minValue")
                    },
                    set: function(e) {
                        this.setPropertyValue("minValue", e) && this.updateData()
                    },
                    enumerable: !0,
                    configurable: !0
                }), Object.defineProperty(t.prototype, "excludeWords", {
                    get: function() {
                        return this.getPropertyValue("excludeWords")
                    },
                    set: function(e) {
                        this.setPropertyValue("excludeWords", e) && this.updateData()
                    },
                    enumerable: !0,
                    configurable: !0
                }), Object.defineProperty(t.prototype, "maxFontSize", {
                    get: function() {
                        return this.getPropertyValue("maxFontSize")
                    },
                    set: function(e) {
                        this.setPropertyValue("maxFontSize", e, !0)
                    },
                    enumerable: !0,
                    configurable: !0
                }), Object.defineProperty(t.prototype, "minFontSize", {
                    get: function() {
                        return this.getPropertyValue("minFontSize")
                    },
                    set: function(e) {
                        this.setPropertyValue("minFontSize", e, !0)
                    },
                    enumerable: !0,
                    configurable: !0
                }), Object.defineProperty(t.prototype, "randomness", {
                    get: function() {
                        return this.getPropertyValue("randomness")
                    },
                    set: function(e) {
                        this.setPropertyValue("randomness", e, !0)
                    },
                    enumerable: !0,
                    configurable: !0
                }), Object.defineProperty(t.prototype, "step", {
                    get: function() {
                        return this.getPropertyValue("step")
                    },
                    set: function(e) {
                        this.setPropertyValue("step", e, !0)
                    },
                    enumerable: !0,
                    configurable: !0
                }), Object.defineProperty(t.prototype, "accuracy", {
                    get: function() {
                        return this.getPropertyValue("accuracy")
                    },
                    set: function(e) {
                        this.setPropertyValue("accuracy", e, !0)
                    },
                    enumerable: !0,
                    configurable: !0
                }), Object.defineProperty(t.prototype, "minWordLength", {
                    get: function() {
                        return this.getPropertyValue("minWordLength")
                    },
                    set: function(e) {
                        this.setPropertyValue("minWordLength", e) && this.updateData()
                    },
                    enumerable: !0,
                    configurable: !0
                }), Object.defineProperty(t.prototype, "rotationThreshold", {
                    get: function() {
                        return this.getPropertyValue("rotationThreshold")
                    },
                    set: function(e) {
                        this.setPropertyValue("rotationThreshold", e, !0)
                    },
                    enumerable: !0,
                    configurable: !0
                }), Object.defineProperty(t.prototype, "angles", {
                    get: function() {
                        return this.getPropertyValue("angles")
                    },
                    set: function(e) {
                        this.setPropertyValue("angles", e, !0)
                    },
                    enumerable: !0,
                    configurable: !0
                }), Object.defineProperty(t.prototype, "maskSprite", {
                    get: function() {
                        return this._maskSprite
                    },
                    enumerable: !0,
                    configurable: !0
                }), t.prototype.copyFrom = function(t) {
                    e.prototype.copyFrom.call(this, t), this.labels.template.copyFrom(t.labels.template)
                }, t.prototype.getWords = function(e) {
                    if (e) {
                        this.dataFields.word = "word", this.dataFields.value = "value";
                        var t = "A-Za-z짧쨉쨘�-횜횠-철첩-��-��-胛康彊叩-姑尻-庫故-枯��-���-科誇-溝狗-��-淘逃-��蘿-��-瘻陋-旒立-�酩-銘袂-��髮-魃房-放舫-蒡謗��-鼈�-裨檳�-蒜杉-森揷西�-西밝ㅍ誓먣쪟-誓□Ⅱ-誓꿋�-誓욈쫭-逝뚟쫸-逝먣쫼-逝ⓣ┴-逝겯┣逝�-逝밝┰鋤롞쭨-鋤앧쭫-鋤□㎞-鋤긍쮨-黍듺쮲-黍먣쮶-黍ⓣØ-黍겯㉡-黍녀㉤-黍뜩㉧-黍밝찙-鼠쒉찠鼠�-鼠닮챸-夕띭첂-夕묂첆-夕ⓣお-夕겯げ-夕녀さ-夕밝そ奭먣쳽-奭□쵃-席뚟쵍-席먣쵑-席ⓣИ-席겯Р-席녀У-席밝Ы惜�-惜앧췃-惜□�昔꺺츉-昔듺츓-昔먣츘-昔뺖츢-昔싟츦昔�-昔잀�-昔ㅰ�-昔む�-昔밝캁析�-析뚟컣-析먣컪-析ⓣ값-析녀갠-析밝갹汐�-汐쇸콬-汐□쾮-淅뚟쾸-淅먣쾼-淅ⓣ꺾-淅녀껨-淅밝꼍潟왽퀬-潟□큶-石뚟킂-石먣킆-石ⓣ눅-石밝늄碩�-碩□돎-碩욈텈-蓆뽤텥-蓆긍떨-蓆삑떽釋�-釋녱툈-錫겯림-錫녀�-仙녱틒-僊귖틖僊�-僊댽틞僊띭틪-僊쀠틯-僊잀벙-僊｀벤僊㏅벳-僊ム벼-僊겯볏-僊녀봄先�-先꾝퍏先�-先앧�嬋�-嬋뉋퐠-嬋о푽-宣뗡��-�め�욉걧-�뺗걳-�앩걾��-�╇겗-�결겣-�곢굨��-�끷깘-�뷘꺖��-�쇹뀩-�㏇넧-�밞�-�댾뎷-�띮뎽-�뽥돇��-�앩돖-�댾뒍-�띮뒓-�결뒼-�듄듃-�얀���-�끷땲-�뽥떂-�먤뙍-�뺗뙓-�싡�-�뤳렆-�닯릟-�п솺-�뜬쉧-�싡슑-�め쎅-�결�-�뚡쐩-�묃쑀-�긔�-�묃씈-�п씙-�결�-�녁윐�쒊젨-聖료�-聲ⓤ˚誠�-誠쒊쪖-醒�Ⅰ-醒닯�-世⒰쭅-勢뉌�-歲뽥쵃-細녁춦-說뗡츆-貰졹�-貰��-嘯ａ콑-塑뤳콢-塑써�-所욉�-消뺗폍-消앩폖-溯끷퐟-溯띮퐧-溯쀡퐰溯쎺퐴溯�-溯써�-瀟닯쓺-瀟솽씨炤�-炤꾞퓛-炤뚡퓧-炤볚퓯-炤쎺퓼-炤п엾-炤닯옅-炤쇄겚�욋굪-�붴꼥�뉍꼯-�볛꽀��-�앪꽕�╈꽙��-��꽢-�밟꽱-�욋뀉-�됤뀕��-�댿�-璲�같-瘦왿콬-瘦�굇-瘦썩�-秀ㅲ�-穗β눗-竪β뎐粹�-粹뽦텭-粹╈땍-粹�떠-粹뜯떳-粹얄�-綏녳톲-綏롡톾-綏뽦퇇-綏왿릭��-�뉎��-�⒲��-�듐��-�쇈걖-�뽧궄-�잆궊-�뷩꺖-�욍꼨-��꽦-�롢넗-�룔눖-�욍�蛾듕�涌껉��-�뚭�-�뚭삉-�잕삫-�リ�-�잕솫-��쇎-�쀪쐵-�잕쑂-�덇엹-�뚭읅-�곴쟽-�낃젃-�딄젋-���-運녠쥉-隕녠쨰-韻κㅀ-蔚녾�-熊④�-雄귢찀-雄뗪�-�ｏ�-箋��-箭わŉ-纏숋�-詮놅쵑-詮쀯쵛詮�-詮⑨И-詮띰Ц-詮쇽Ь輾�-輾곻춢-輾꾬춨-轉깍캆-顚쏙탳-餞륅텙-切뉛럭-切삼물-折댐뭉-癤쇽샥-竊븝퐗-節싷쉐-絶억퓗-占뉛퓡-占륅퓪-占쀯퓴-占�0-9@+",
                            i = new RegExp("([" + t + "]+[-" + t + "]*[" + t + "]+)|([" + t + "]+)", "ig"),
                            r = e.match(i);
                        if (!r) return [];
                        for (var a = [], n = void 0; n = r.pop();) {
                            for (var s = void 0, o = 0; o < a.length; o++)
                                if (a[o].word.toLowerCase() == n.toLowerCase()) {
                                    s = a[o];
                                    break
                                } s ? (s.value++, this.isCapitalized(n) || (s.word = n)) : a.push({
                                word: n,
                                value: 1
                            })
                        }
                        var l = this.excludeWords;
                        if (this.minValue > 1 || this.minWordLength > 1 || l && l.length > 0)
                            for (o = a.length - 1; o >= 0; o--) {
                                var u = a[o];
                                u.value < this.minValue && a.splice(o, 1), u.word.length < this.minWordLength && a.splice(o, 1), -1 !== l.indexOf(u.word) && a.splice(o, 1)
                            }
                        return a.sort(function(e, t) {
                            return e.value == t.value ? 0 : e.value > t.value ? -1 : 1
                        }), a.length > this.maxCount && (a = a.slice(0, this.maxCount)), a
                    }
                }, t.prototype.isCapitalized = function(e) {
                    var t = e.toLowerCase();
                    return e[0] != t[0] && e.substr(1) == t.substr(1) && e != t
                }, t
            }(s.a);
        c.c.registeredClasses.WordCloudSeries = x, c.c.registeredClasses.WordCloudSeriesDataItem = v;
        var _ = function(e) {
                function t() {
                    var t = e.call(this) || this;
                    return t.className = "WordCloudDataItem", t.applyTheme(), t
                }
                return Object(a.c)(t, e), t
            }(n.b),
            C = function(e) {
                function t() {
                    var t = e.call(this) || this;
                    return t.className = "WordCloud", t.seriesContainer.isMeasured = !0, t.seriesContainer.layout = "absolute", t._usesData = !0, t.applyTheme(), t
                }
                return Object(a.c)(t, e), t.prototype.createSeries = function() {
                    return new x
                }, t
            }(n.a);
        c.c.registeredClasses.WordCloud = C, c.c.registeredClasses.WordCloudDataItem = _, window.am4plugins_wordCloud = r
    }
}, ["6JTK"]);