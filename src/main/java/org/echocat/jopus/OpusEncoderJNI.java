/*****************************************************************************************
 * *** BEGIN LICENSE BLOCK *****
 *
 * Version: MPL 2.0
 *
 * echocat JOpus, Copyright (c) 2014 echocat
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * *** END LICENSE BLOCK *****
 ****************************************************************************************/

package org.echocat.jopus;

final class OpusEncoderJNI extends OpusJNISupport {

    private OpusEncoderJNI() {
    }

    /**
     * <p>Allocates and initializes an encoder state.</p>
     * <p/>
     * <p>Regardless of the sampling rate and number channels selected, the Opus encoder
     * can switch to a lower audio bandwidth or number of channels if the bitrate
     * selected is too low. This also means that it is safe to always use 48 kHz stereo input
     * and let the encoder optimize the encoding.</p>
     *
     * @param samplingRateInHz Sampling rate of input signal (Hz) This must be one of 8000, 12000, 16000, 24000 or 48000.
     * @param numberOfChannels Number of channels (1 or 2) in input signal
     * @param application      Coding mode
     * @return The encoder handle.
     */
    static native long create(int samplingRateInHz, int numberOfChannels, int application);

    /**
     * <p>Initializes a previously allocated encoder state</p>
     *
     * @param samplingRateInHz Sampling rate of input signal (Hz) This must be one of 8000, 12000, 16000, 24000 or 48000.
     * @param numberOfChannels Number of channels (1 or 2) in input signal
     * @param application      Coding mode
     */
    static native void init(long encoderHandle, int samplingRateInHz, int numberOfChannels, int application);

    /**
     * Encodes an Opus frame.
     *
     * @param pcm        Input signal (interleaved if 2 channels). length is
     *                   <code>frameSize</code>*<code>channels</code>
     * @param frameSize  Number of samples per channel in the input signal. This must be an Opus frame size for the
     *                   encoder's sampling rate. For example, at 48 kHz the permitted values are 120, 240, 480, 960,
     *                   1920, and 2880. Passing in a duration of less than 10 ms (480 samples at 48 kHz) will prevent
     *                   the encoder from using the LPC or hybrid modes.
     * @param packet       Output payload. This must contain storage for at least <code>opusLength</code>.
     * @param packetLength Size of the allocated memory for the output payload. This may be used to impose an upper limit
     *                   on the instant bitrate, but should not be used as the only bitrate control. Use
     *                   {@link #setBitRate} to control the bitrate.
     * @return The length of the encoded packet (in bytes).
     */
    static native int encode(long encoderHandle, short[] pcm, int frameSize, byte[] packet, int packetLength);

    /**
     * Encodes an Opus frame from floating point input.
     *
     * @param pcm        Input signal (interleaved if 2 channels). length is
     *                   <code>frameSize</code>*<codechannels</code>
     * @param frameSize  Number of samples per channel in the input signal. This must be an Opus frame size for the
     *                   encoder's sampling rate. For example, at 48 kHz the permitted values are 120, 240, 480, 960,
     *                   1920, and 2880. Passing in a duration of less than 10 ms (480 samples at 48 kHz) will prevent
     *                   the encoder from using the LPC or hybrid modes.
     * @param packet     Output payload. This must contain storage for at least <code>opusLength</code>.
     * @param packetLength Size of the allocated memory for the output payload. This may be used to impose an upper limit
     *                   on the instant bitrate, but should not be used as the only bitrate control. Use
     *                   <code>#OPUS_SET_BITRATE</code> to control the bitrate.
     * @return The length of the encoded packet (in bytes).
     */
    static native int encodeFloat(long encoderHandle, float[] pcm, int frameSize, byte[] packet, int packetLength);

    /**
     * <p>Frees an opus encoder allocated by {@link #create(int, int, int)}.</p>
     */
    static native void destroy(long encoderHandle);

    /**
     * @return A value between 0-10 (inclusive), with 10 representing the highest complexity.
     */
    static native int getComplexity(long encoderHandle);

    /**
     * @see #getComplexity
     */
    static native void setComplexity(long encoderHandle, int value);

    /**
     * @return <p>Rates from 500 to 512000 bits per second are meaningful, as well as the special values
     * {@link OpusConstants#AUTO} and {@link OpusConstants#BIT_RATE_MAX}. The value {@link OpusConstants#BIT_RATE_MAX}
     * can be used to cause the codec to use as much rate as it can, which is useful for controlling the rate by
     * adjusting the output buffer size.</p>
     *
     * <p>The default is determined based on the number of channels and the input sampling rate.</p>
     */
    static native int getBitRate(long encoderHandle);

    /**
     * @see #getBitRate
     */
    static native void setBitRate(long encoderHandle, int value);

    /**
     * @return <p>the state of the variable bitrate (VBR) in the encoder.</p>
     *
     * <p>The configured bitrate may not be met exactly because frames must be an integer number of bytes in length.</p>
     *
     * <p><b>Warning:</b> Only the MDCT mode of Opus can provide hard CBR behavior.</p>
     *
     * <p><b>Possible values:</b><ul>
     *      <li>0: Hard CBR. For LPC/hybrid modes at very low bit-rate, this can cause noticeable quality degradation.</li>
     *      <li>1: VBR (default). The exact type of VBR is controlled by {@link #setVbrConstraint}.</li>
     * </ul></p>
     */
    static native int getVbr(long encoderHandle);

    /**
     * @see #getVbr
     */
    static native void setVbr(long encoderHandle, int value);

    /**
     * @return <p>the state of the constrained VBR in the encoder.</p>
     *
     * <p>This setting is ignored when the encoder is in CBR mode.</p>
     *
     * <b>Warning:</b> Only the MDCT mode of Opus currently heeds the constraint.</p>
     *
     * <p>Speech mode ignores it completely, hybrid mode may fail to obey it if the LPC layer uses more bitrate than the
     * constraint would have permitted.</p>
     *
     * <p><b>Possible values:</b><ul>
     *      <li>0: Unconstrained VBR.</li>
     *      <li>1: Constrained VBR (default). This creates a maximum of one frame of buffering delay assuming a
     *      transport with a serialization speed of the nominal bitrate.</li>
     * </ul></p>
     */
    static native int getVbrConstraint(long encoderHandle);

    /**
     * @see #getVbrConstraint
     */
    static native void setVbrConstraint(long encoderHandle, int value);

    /**
     * @return <p>the state of mono/stereo forcing in the encoder.</p>
     *
     * <p>This can force the encoder to produce packets encoded as either mono or stereo, regardless of the format of
     * the input audio. This is useful when the caller knows that the input signal is currently a mono source embedded
     * in a stereo stream.</p>
     *
     * <p><b>Possible values:</b><ul>
     *      <li>{@link OpusConstants#AUTO}: Not forced (default)</li>
     *      <li>1: Forced mono</li>
     *      <li>2: Forced stereo</li>
     * </ul></p>
     */
    static native int getForceChannels(long encoderHandle);

    /**
     * @see #getForceChannels
     */
    static native void setForceChannels(long encoderHandle, int value);

    /**
     * @return <p>the maximum bandpass that the encoder will select automatically.</p>
     *
     * <p>Applications should normally use this instead of {@link #setBandwidth} (leaving that set to the default,
     * {@link OpusConstants#AUTO}). This allows the application to set an upper bound based on the type of input it is
     * providing, but still gives the encoder the freedom to reduce the bandpass when the bitrate becomes too low, for
     * better overall quality.</p>
     *
     * <p><b>Possible values:</b><ul>
     *      <li>{@link OpusConstants#BANDWIDTH_NARROWBAND}: 4 kHz passband</li>
     *      <li>{@link OpusConstants#BANDWIDTH_MEDIUMBAND}: 6 kHz passband</li>
     *      <li>{@link OpusConstants#BANDWIDTH_WIDEBAND}: 8 kHz passband</li>
     *      <li>{@link OpusConstants#BANDWIDTH_SUPERWIDEBAND}: 12 kHz passband</li>
     *      <li>{@link OpusConstants#BANDWIDTH_FULLBAND}: 20 kHz passband (default)</li>
     * </ul></p>
     */
    static native int getMaxBandwidth(long encoderHandle);

    /**
     * @see #getMaxBandwidth
     */
    static native void setMaxBandwidth(long encoderHandle, int value);

    /**
     * @return <p>the type of signal being encoded.</p>
     * 
     * <p>This is a hint which helps the encoder's mode selection.</p>
     *
     * <p><b>Possible values:</b><ul>
     *      <li>{@link OpusConstants#AUTO}: (default)</li>
     *      <li>{@link OpusConstants#SIGNAL_VOICE}: Bias thresholds towards choosing LPC or Hybrid modes.</li>
     *      <li>{@link OpusConstants#SIGNAL_MUSIC}: Bias thresholds towards choosing MDCT modes.</li>
     * </ul></p>
     */
    static native int getSignal(long encoderHandle);

    /**
     * @see #getSignal
     */
    static native void setSignal(long encoderHandle, int value);

    /**
     * @return <p>the encoder's intended application.</p>
     *
     * <p><b>Possible values:</b><ul>
     *      <li>{@link OpusConstants#APPLICATION_VOIP}: Process signal for improved speech intelligibility.</li>
     *      <li>{@link OpusConstants#APPLICATION_AUDIO}: Favor faithfulness to the original input.</li>
     *      <li>{@link OpusConstants#APPLICATION_RESTRICTED_LOW_DELAY}: Configure the minimum possible coding delay by
     *      disabling certain modes of operation.</li>
     * </ul></p>
     */
    static native int getApplication(long encoderHandle);

    /**
     * @see #getApplication
     */
    static native void setApplication(long encoderHandle, int value);

    /**
     * @return <p>The sampling rate the encoder or decoder was initialized with.</p>
     *
     * <p>This value could only be set by using {@link #create} or {@link #init}.</p>
     */
    static native int getSampleRate(long encoderHandle);

    /**
     * @return <p>The total samples of delay added by the entire codec.
     * This can be queried by the encoder and then the provided number of samples can be
     * skipped on from the start of the decoder's output to provide time aligned input
     * and output. From the perspective of a decoding application the real data begins this many
     * samples late.</p>
     *
     * <p>The decoder contribution to this delay is identical for all decoders, but the
     * encoder portion of the delay may vary from implementation to implementation,
     * version to version, or even depend on the encoder's initial configuration.
     * Applications needing delay compensation should call this CTL rather than
     * hard-coding a value.</p>
     */
    static native int getLookAhead(long encoderHandle);

    /**
     * @return <p>the encoder's use of inband forward error correction (FEC).</p>
     *
     * <p><b>Note:</b> This is only applicable to the LPC layer</p>
     *
     * <p><b>Possible values:</b><ul>
     *      <li>0: Disable inband FEC (default).</li>
     *      <li>1: Enable inband FEC.</li>
     * </ul></p>
     */
    static native int getInBandFec(long encoderHandle);

    /**
     * @see #getInBandFec
     */
    static native void setInBandFec(long encoderHandle, int value);

    /**
     * @return <p>the encoder's expected packet loss percentage.</p>
     *
     * <p>Higher values with trigger progressively more loss resistant behavior in the encoder
     * at the expense of quality at a given bitrate in the lossless case, but greater quality
     * under loss.</p>
     *
     * <p>Possible values are between 0 and 100.</p>
     */
    static native int getPacketLossPerc(long encoderHandle);

    /**
     * @see #getPacketLossPerc
     */
    static native void setPacketLossPerc(long encoderHandle, int value);

    /**
     * @return <p>the encoder's use of discontinuous transmission (DTX).</p>
     *
     * <p><b>Note:</b> This is only applicable to the LPC layer</p>
     *
     * <p><b>Possible values:</b><ul>
     *      <li>0: Disable DTX (default).</li>
     *      <li>1: Enable DTX.</li>
     * </ul></p>
     */
    static native int getDtx(long encoderHandle);

    /**
     * @see #getDtx
     */
    static native void setDtx(long encoderHandle, int value);

    /**
     * @return <p>the depth of signal being encoded.</p>
     *
     * <p>This is a hint which helps the encoder identify silence and near-silence.</p>
     *
     * <p>Possible values are between 8 and 24. (default: 24)</p>
     */
    static native int getLsbDepth(long encoderHandle);

    /**
     * @see #getLsbDepth
     */
    static native void setLsbDepth(long encoderHandle, int value);

    /**
     * @return <p>the encoder's use of variable duration frames.</p>
     *
     * <p>When variable duration is enabled, the encoder is free to use a shorter frame size than the one requested in
     * the {@link #encode} call. It is then the user's responsibility to verify how much audio was encoded by checking
     * the ToC byte of the encoded packet. The part of the audio that was not encoded needs to be resent to the encoder
     * for the next call. Do not use this option unless you <b>really</b> know what you are doing.</p>
     *
     * <p><b>Possible values:</b><ul>
     *      <li>{@link OpusConstants#FRAMESIZE_ARG}: Select frame size from the argument (default).</li>
     *      <li>{@link OpusConstants#FRAMESIZE_2_5_MS}</li>
     *      <li>{@link OpusConstants#FRAMESIZE_5_MS}</li>
     *      <li>{@link OpusConstants#FRAMESIZE_10_MS}</li>
     *      <li>{@link OpusConstants#FRAMESIZE_20_MS}</li>
     *      <li>{@link OpusConstants#FRAMESIZE_40_MS}</li>
     *      <li>{@link OpusConstants#FRAMESIZE_VARIABLE}</li>
     * </ul></p>
     */
    static native int getExpertFrameDuration(long encoderHandle);

    /**
     * @see #getExpertFrameDuration
     */
    static native void setExpertFrameDuration(long encoderHandle, int value);

    /**
     * @return <p>If 1, disables almost all use of prediction, making frames almost completely independent. This
     * reduces quality. (default : 0)</p>
     */
    static native int getPredictionDisabled(long encoderHandle);

    /**
     * @see #getPredictionDisabled
     */
    static native void setPredictionDisabled(long encoderHandle, int value);

    /**
     * @return <p>the final state of the codec's entropy coder.</p>
     *
     * <p>This is used for testing purposes, The encoder and decoder state should be identical after coding a payload
     * (assuming no data corruption or software bugs)</p>
     */
    static native int getFinalRange(long encoderHandle);

    /**
     * @return <p>the encoder's bandpass to a specific value.</p>
     *
     * <p>This prevents the encoder from automatically selecting the bandpass based on the available bitrate. If an
     * application knows the bandpass of the input audio it is providing, it should normally use
     * {@link #setMaxBandwidth} instead, which still gives the encoder the freedom to reduce the bandpass when the
     * bitrate becomes too low, for better overall quality.</p>
     *
     * <p><b>Possible values:</b><ul>
     *      <li>{@link OpusConstants#AUTO}: (default)</li>
     *      <li>{@link OpusConstants#BANDWIDTH_NARROWBAND}: 4 kHz passband</li>
     *      <li>{@link OpusConstants#BANDWIDTH_MEDIUMBAND}: 6 kHz passband</li>
     *      <li>{@link OpusConstants#BANDWIDTH_WIDEBAND}: 8 kHz passband</li>
     *      <li>{@link OpusConstants#BANDWIDTH_SUPERWIDEBAND}: 12 kHz passband</li>
     *      <li>{@link OpusConstants#BANDWIDTH_FULLBAND}: 20 kHz passband (default)</li>
     * </ul></p>
     */
    static native int getBandwidth(long encoderHandle);

    /**
     * @see #getBandwidth
     */
    static native void setBandwidth(long encoderHandle, int value);

}
