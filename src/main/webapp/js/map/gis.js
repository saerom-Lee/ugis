/**
 * @author bys
 * @since 2021.09.24
 * @alias 맵 선택기능.
 * @type {Select}
 */
const Select = class {
    constructor(uMap) {

        this.dMap = uMap;
        this.map = this.dMap;
        this.source = new ol.source.Vector;
        this.layer = this.createLayer();
        this.map.addLayer(this.layer);
        this.interaction = null;
        this.interactions = [];
    }
    clearSource() {
        this.source.clear();
    }

    createLayer() {
        const layer = new ol.layer.Vector({
            source: this.source,
            zIndex: 500,
        });
        return layer;
    }
    addInteraction(type) {

        let options = {
            source: this.source,
        };
        if (type == "Rect") {
            options.type = "Circle";
            options.geometryFunction = ol.interaction.Draw.createRegularPolygon(4);
        } else if (type == "Box") {
            options.type = "Circle";
            options.geometryFunction = ol.interaction.Draw.createBox();
        } else {
            options.type = type;
        }

        this.interaction = new ol.interaction.Draw(options);
        this.map.addInteraction(this.interaction);
        this.interactions.push(this.interaction);
    }

    /**
     * 상호작용 초기화
     */
    clearInteraction() {
        this.interactions.forEach((interaction) => {

            this.map.removeInteraction(interaction);
        });
        this.interactions = [];
      //  this.notify();
    }

    setInteractions(interactions) {
        this.clearInteraction();
        interactions.forEach((interaction) => {
            this.addInteraction(interaction);
        });
        this.interactions = interactions;
    }
    once(type, eventType, callback, isRemove) {
        const that = this;
        //that.clear();

        this.addInteraction(type);

        if (eventType && callback) {
            this.interaction.once(eventType, function (event) {
                callback(event);
                that.clearInteraction();
                if (isRemove) {
                    setTimeout(function () {
                        that.clearSource();
                    }, 100);
                }
            });
        }
    }
}


