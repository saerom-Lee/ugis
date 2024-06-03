/**
 * 
 */
CMSC003.Util = (() => {
	return {
		base64ToBlob(b64Data, contentType = '', sliceSize = 512) {
			if (!b64Data) {
				return;
			}
			const byteCharacters = atob(b64Data.replace(/\"/gi, "")); 
			const byteArrays = [];

			for (let offset = 0; offset < byteCharacters.length; offset += sliceSize) {
				const slice = byteCharacters.slice(offset, offset + sliceSize);

				const byteNumbers = new Array(slice.length);
				for (let i = 0; i < slice.length; i++) {
					byteNumbers[i] = slice.charCodeAt(i);
				}

				const byteArray = new Uint8Array(byteNumbers);
				byteArrays.push(byteArray);
			}

			const blob = new Blob(byteArrays, { type: contentType });
			return blob;
		},
		bytesToBlob(byteCharacters, contentType = '', sliceSize = 512) {
			if (!byteCharacters) {
				return;
			}
			const byteArrays = [];

			for (let offset = 0; offset < byteCharacters.length; offset += sliceSize) {
				const slice = byteCharacters.slice(offset, offset + sliceSize);

				const byteNumbers = new Array(slice.length);
				for (let i = 0; i < slice.length; i++) {
					byteNumbers[i] = slice.charCodeAt(i);
				}

				const byteArray = new Uint8Array(byteNumbers);
				byteArrays.push(byteArray);
			}

			const blob = new Blob(byteArrays, { type: contentType });
			return blob;
		},
	}
})();